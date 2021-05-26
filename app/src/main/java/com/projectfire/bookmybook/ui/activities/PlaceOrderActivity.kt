package com.projectfire.bookmybook.ui.activities

import android.content.Intent
import android.graphics.fonts.SystemFonts
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.CartItem
import com.projectfire.bookmybook.models.Order
import com.projectfire.bookmybook.models.Product
import com.projectfire.bookmybook.models.User
import com.projectfire.bookmybook.ui.adapters.CartItemsPlaceOrderListAdapter
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaceOrderActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartList: ArrayList<CartItem>
    private lateinit var mUser: User
    private var mSubTotal = 0.0
    private var mTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)

        setupActionBar()
        getFireStoreData()

        btn_place_order.setOnClickListener {
            placeOrder()
        }
    }

    private fun getFireStoreData() {
        getUserDetails()
    }

    private fun setupActionBar() {                       // Remember this method
        setSupportActionBar(toolbar_place_order_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = ""
        }

        toolbar_place_order_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseFunctionsClass().getUserDetails(this@PlaceOrderActivity)
    }

    fun userDetailsSuccess(user: User) {

        tv_checkout_address.text = "${user.address}, ${user.pin}"
        tv_checkout_full_name.text = "${user.firstName} ${user.lastName}"
        tv_checkout_mobile_number.text = user.mobile.toString()

        mUser = user
        getProductList()
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        mProductsList = productList
        getCartItemsList()
    }

    fun getProductList() {
        FirebaseFunctionsClass().getAllProductsList(this@PlaceOrderActivity)
    }

    private fun getCartItemsList() {
        FirebaseFunctionsClass().getCartList(this@PlaceOrderActivity)
    }

    fun successCartItemsList(cartlist: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductsList) {
            for (item in cartlist) {
                if (product.product_id == item.product_id) {
                    item.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartList = cartlist

        rv_cart_list_items.layoutManager = LinearLayoutManager(this@PlaceOrderActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsPlaceOrderListAdapter(this@PlaceOrderActivity, mCartList)

        rv_cart_list_items.adapter = cartListAdapter

        for (item in mCartList) {
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += price * quantity
            }
        }

        var temp1 = (mSubTotal * 100).toInt()
        mSubTotal = (temp1.toDouble()) / 100.0

        tv_checkout_sub_total.text = "₹$mSubTotal"
        tv_checkout_service_charge.text = "₹50.0"

        if (mSubTotal > 0) {
            tv_no_cart_item_found.visibility = View.GONE
            rv_cart_list_items.visibility = View.VISIBLE
            ll_checkout_place_order.visibility = View.VISIBLE
            mTotal = mSubTotal + 50.0
            var temp2 = (mTotal * 100).toInt()
            mTotal = (temp2.toDouble()) / 100.0
            tv_checkout_total_amount.text = "₹$mTotal"
        } else {
            tv_no_cart_item_found.visibility = View.VISIBLE
            rv_cart_list_items.visibility = View.GONE
            ll_checkout_place_order.visibility = View.GONE
        }

    }

    private fun placeOrder() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val date = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(date, Locale.getDefault())
        val calendarInstance: Calendar = Calendar.getInstance()
        calendarInstance.timeInMillis = System.currentTimeMillis()
        val orderDate = formatter.format(calendarInstance.time)

        val userId = FirebaseFunctionsClass().getCurrentUserID()
        val order = Order(
            userId,
            "${mUser.firstName} ${mUser.lastName}",
            mCartList,
            mUser.address,
            mUser.pin.toString(),
            mUser.mobile.toString(),
            "Order of ${mUser.firstName} ${mUser.lastName} (${userId}) at ${orderDate}",
            mCartList[0].image,
            mSubTotal.toString(),
            "50.0",
            mTotal.toString(),
            orderDate,
            "${mUser.firstName}/${System.currentTimeMillis()}"
        )

        FirebaseFunctionsClass().placeOrder(this@PlaceOrderActivity, order)
    }

    fun orderSuccess() {
        FirebaseFunctionsClass().updateDetails(this, mCartList)
    }

    fun detailsUpdatedSuccessfully() {
        hideProgressDialog()
        showSnackBar("Your order was successfully placed", false)

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                val intent = Intent(this@PlaceOrderActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            },
            1500
        )
    }
}