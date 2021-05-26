package com.projectfire.bookmybook.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectfire.bookmybook.Constants
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import com.projectfire.bookmybook.models.Sold
import com.projectfire.bookmybook.ui.adapters.CartItemsPlaceOrderListAdapter
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_details.toolbar_order_details_activity
import kotlinx.android.synthetic.main.activity_sold_details.*

class SoldDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_details)

        var productDetails: Sold

        if(intent.hasExtra(Constants.EXTRA_SOLD_DETAILS)){
            productDetails=
                intent.getParcelableExtra(Constants.EXTRA_SOLD_DETAILS)!!

            setupActionBar()
            getUI(productDetails)
        }
    }
    private fun setupActionBar() {                       // Remember this method
        setSupportActionBar(toolbar_sold_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = ""
        }
        toolbar_sold_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUI(sold: Sold) {
        GlideLoader(this).loadProductPicture(sold.image,iv_sold_items)
        tv_sold_id.text = ""
        tv_sold_full_name.text = sold.name
        tv_sold_date.text = sold.order_date
        tv_sold_address.text = "${sold.address}, ${sold.pin}"
        tv_sold_mobile_number.text =sold.mobile
        tv_sold_total_amount.text= "₹${sold.price}"
    }
}