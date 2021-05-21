package com.projectfire.bookmybook.firestore

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
import com.projectfire.bookmybook.models.Product
import com.projectfire.bookmybook.models.User
import com.projectfire.bookmybook.ui.activities.*
import com.projectfire.bookmybook.ui.fragments.BuyFragment
import com.projectfire.bookmybook.ui.fragments.SellFragment
import com.projectfire.bookmybook.utilities.Constants

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        //If collection is not present, it will create it
        mFirestore.collection(Constants.USERS)
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
        mFirestore.collection(Constants.USERS)
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
//                    is SettingsActivity -> {
//                        activity.userDetailsSuccess(user)
//                    }
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
        mFirestore.collection(Constants.USERS)
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

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "${Constants.USER_PROFILE_IMAGE}_${FirebaseAuth.getInstance().currentUser!!.uid}_${System.currentTimeMillis()}.${
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
                    }
                }
        }
            .addOnFailureListener { exception ->

                when (activity) {
                    is UserProfileActivity -> {
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

                        mFirestore.collection(Constants.USERS)
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

                                        mFirestore.collection(Constants.USERS)
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
                                            mFirestore.collection(
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
            mFirestore.collection(Constants.PRODUCTS)
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
            mFirestore.collection(Constants.PRODUCT)
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
            mFirestore.collection((Constants.PRODUCT))
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

        fun deleteProduct(fragment: SellFragment, productId: String) {
            mFirestore.collection(Constants.PRODUCT)
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
        } }