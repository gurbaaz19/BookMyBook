package com.projectfire.bookmybook.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectfire.bookmybook.Constants
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_product_details.*

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        setupActionBar()

        var orderDetails: Order

        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS)) {
            orderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_ORDER_DETAILS)!!
            getUI(orderDetails)
        }

    }

    private fun setupActionBar() {                       // Remember this method
        setSupportActionBar(toolbar_order_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = ""
        }
        toolbar_order_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUI(order: Order){

    }
}