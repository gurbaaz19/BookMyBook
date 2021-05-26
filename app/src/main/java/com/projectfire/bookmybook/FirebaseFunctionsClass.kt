package com.projectfire.bookmybook

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.projectfire.bookmybook.models.*
import com.projectfire.bookmybook.ui.activities.*
import com.projectfire.bookmybook.ui.fragments.BuyFragment
import com.projectfire.bookmybook.ui.fragments.TransactionFragment
import com.projectfire.bookmybook.ui.fragments.SellFragment

class FirebaseFunctionsClass {

    private val mFirestoreInstance = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        //If collection is not present, it will create it
        mFirestoreInstance.collection(Constants.USERS)
            //Getting document id, here it is same as id
            .document(userInfo.id)
            //If we want to merge the data instead of replacing the complete thing
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
//                activity.showErrorSnackBar( "Error while registering the user.", true)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        //collection name
        mFirestoreInstance.collection(Constants.USERS)
            //get document id
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.APP_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //KEY: "logged_in_username"
                //VALUE: "${user.firstName} ${user.lastName}"
                editor.putString(
                    Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is PlaceOrderActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error while getting user details", e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFirestoreInstance.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                        activity.showSnackBar("Error while updating the user details", true)
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while updating the user details", e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "${imageType}_${getCurrentUserID()}_${System.currentTimeMillis()}.${
                Constants.getFileExtension(
                    activity,
                    imageFileURI
                )
            }"
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            //get downloadable uri
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Image URL", uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProductActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener { exception ->

                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName, exception.message, exception
                )
            }
    }


    fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        when (activity) {
            is LoginActivity -> {
                var auth = Firebase.auth

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->

                        var isRegistered = false

                        mFirestoreInstance.collection(Constants.USERS)
                            .whereEqualTo(Constants.ID, auth.currentUser?.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                Log.e("A", document.documents.size.toString())
                                if (document.documents.size > 0) {
                                    isRegistered = true
                                }

                                if (task.isSuccessful) {
                                    Log.e("A", isRegistered.toString())
                                    if (!isRegistered) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("A", "signInWithCredential:success")
                                        var name = auth.currentUser!!.displayName!!.split(" ")
                                        val userInfo = User(
                                            auth.currentUser!!.uid,
                                            name[0],
                                            name[name.size - 1],
                                            auth.currentUser!!.email!!
                                        )

                                        mFirestoreInstance.collection(Constants.USERS)
                                            //Getting document id, here it is same as id
                                            .document(userInfo.id)
                                            //If we want to merge the data instead of replacing the complete thing
                                            .set(userInfo, SetOptions.merge())
                                            .addOnSuccessListener {
                                                activity.userLoggedInSuccess(userInfo)
                                            }
                                            .addOnFailureListener { e ->
                                                activity.hideProgressDialog()
                                                Log.e(
                                                    activity.javaClass.simpleName,
                                                    "Error while registering the user.",
                                                    e
                                                )
                                            }
                                    } else if (isRegistered) {
                                        var user: Task<DocumentSnapshot> =
                                            mFirestoreInstance.collection(
                                                Constants.USERS
                                            ).document(getCurrentUserID()).get()
                                        user.addOnSuccessListener { document ->
                                            val userInfo =
                                                document.toObject(User::class.java)!!
                                            activity.userLoggedInSuccess(userInfo)
                                        }

                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("A", "signInWithCredential:failure", task.exception)
                                    activity.showSnackBar("Authentication Failed", true)
                                    //updateUI(null)
//                activity.showErrorSnackBar( "Error while registering the user.", true)
                                }
                            }
                    }
            }
        }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {
        //If collection is not present, it will create it
        mFirestoreInstance.collection(Constants.PRODUCTS)
            //Getting document id, here it is same as id
            .document()
            //If we want to merge the data instead of replacing the complete thing
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product.",
                    e
                )
//                activity.showErrorSnackBar( "Error while registering the user.", true)
            }
    }

    fun getProductsList(fragment: Fragment) {
        mFirestoreInstance.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (fragment) {
                    is SellFragment -> {
                        fragment.successProductsListFromFireStore((productsList))
                    }
                }
            }
    }

    fun getDashboardItemsList(fragment: BuyFragment) {
        mFirestoreInstance.collection((Constants.PRODUCTS))
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }

                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener { e ->

                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard item list.", e)
            }
    }

    fun deleteProduct(fragment: SellFragment, productId: String): Boolean {
        mFirestoreInstance.collection(Constants.PRODUCTS).document(productId).get()
            .addOnSuccessListener { document ->
                var item = document.toObject(Product::class.java)

                if (item != null) {
                    Firebase.storage.getReferenceFromUrl(item.image).delete()
                        .addOnSuccessListener { deleteProductEntry(fragment, productId) }
                }
            }
        return true
    }

    fun deleteProductEntry(fragment: SellFragment, productId: String) {

        mFirestoreInstance.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener { fragment.productDeleteSuccess() }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting product",
                    e
                )
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {
        mFirestoreInstance.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting product details", e)
            }

    }

    fun addCartItems(activity: ProductDetailsActivity, cartItem: CartItem) {
        mFirestoreInstance.collection(Constants.CART_ITEMS)
            .document()
            .set(cartItem, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: String) {
        mFirestoreInstance.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }

    fun getCartList(activity: Activity) {
        mFirestoreInstance.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<CartItem> = ArrayList()

                for (i in document.documents) {
                    val cartItem = i.toObject(CartItem::class.java)!!

                    cartItem.id = i.id
                    list.add(cartItem)
                }

                when (activity) {
                    is CartListActivity -> {
                        activity.successCartItemList(list)
                    }

                    is PlaceOrderActivity -> {
                        activity.successCartItemsList(list)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is PlaceOrderActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error while getting the cart list items", e)
            }
    }

    fun getAllProductsList(activity: Activity) {
        mFirestoreInstance.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                when (activity) {
                    is CartListActivity -> {
                        activity.successProductsListFromFireStore(productsList)
                    }
                    is PlaceOrderActivity -> {
                        activity.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is PlaceOrderActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting all product list.", e)
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {
        mFirestoreInstance.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error while removing the item from cart", e)
            }
    }

    fun updateMyCart(context: Context, card_id: String, itemHashMap: HashMap<String, Any>) {
        mFirestoreInstance.collection(Constants.CART_ITEMS)
            .document(card_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.updateItemSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }

                Log.e(context.javaClass.simpleName, "Error while updating the cart item", e)
            }
    }

    fun placeOrder(activity: PlaceOrderActivity, order: Order) {
        mFirestoreInstance.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while placing order", e)
            }

    }

    fun updateDetails(activity: PlaceOrderActivity, cartList: ArrayList<CartItem>, order: Order) {
        val writeBatch = mFirestoreInstance.batch()
        for (item in cartList) {

            val sold = Sold(
                item.product_owner_id,
                item.title,
                item.price,
                item.cart_quantity,
                item.image,
                order.title,
                order.date,
                order.sub_total_amount,
                order.service_charge,
                order.total_amount,
                order.address,
                order.pin,
                order.user_name,
                order.mobile,
/**/
            )

            val documentReference = mFirestoreInstance.collection(Constants.SOLD)
                .document(item.product_id)

            writeBatch.set(documentReference, sold)
        }

        for (item in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.STOCK_QUANTITY] =
                (item.stock_quantity.toInt() - item.cart_quantity.toInt()).toString()

            val documentReference = mFirestoreInstance.collection(Constants.PRODUCTS)
                .document(item.product_id)

            writeBatch.update(documentReference, productHashMap)
        }

        for (item in cartList) {
            val documentReference = mFirestoreInstance.collection(Constants.CART_ITEMS)
                .document(item.id)

            writeBatch.delete(documentReference)
        }

        writeBatch.commit()
            .addOnSuccessListener {
                activity.detailsUpdatedSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while updating details", e)
            }

    }

    fun getOrdersList(fragment: TransactionFragment) {
        mFirestoreInstance.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val orderList: ArrayList<Order> = ArrayList()

                for (i in document.documents) {
                    val item = i.toObject(Order::class.java)!!
                    item.id = i.id

                    orderList.add(item)
                }
                fragment.successGetOrderUI(orderList)
            }
            .addOnFailureListener { e ->

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting order details", e)
            }
    }

    fun getSoldList(fragment: TransactionFragment) {
        mFirestoreInstance.collection(Constants.SOLD)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val soldList: ArrayList<Sold> = ArrayList()

                for (i in document.documents) {
                    val item = i.toObject(Sold::class.java)!!
                    item.id = i.id

                    soldList.add(item)
                }
                fragment.successGetSoldUI(soldList)
            }
            .addOnFailureListener { e ->

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting sold details", e)
            }
    }

}