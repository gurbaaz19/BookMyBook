package com.projectfire.bookmybook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.CartItem
import com.projectfire.bookmybook.models.Product
import com.projectfire.bookmybook.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_settings.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this,PlaceOrderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductList()
    }

    private fun setupActionBar() {                       // Remember this method
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getCartItemsList() {
        FirebaseFunctionsClass().getCartList(this@CartListActivity)
    }

    fun successCartItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItems)
            rv_cart_items_list.adapter = cartListAdapter
            var subTotal = 0.0
            var service = 50.0

            for (item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()
                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }
            }

//            var temp1 = (gst*100).toInt()
//            gst = (temp1.toDouble())/100.0

            tv_sub_total.text = "₹$subTotal"
            tv_gst.text = "₹$service"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                var total = subTotal + service

                var temp2 = (total * 100).toInt()
                total = (temp2.toDouble()) / 100.0

                tv_total_amount.text = "₹$total"
            }
            } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseFunctionsClass().getAllProductsList(this@CartListActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        //hideProgressDialog()
        mProductsList = productsList

        getCartItemsList()
    }

    fun itemRemovedSuccess() {
        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    fun updateItemSuccess() {
       // hideProgressDialog()
        getCartItemsList()
    }
}