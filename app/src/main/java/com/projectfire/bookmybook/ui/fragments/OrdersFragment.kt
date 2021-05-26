package com.projectfire.bookmybook.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import com.projectfire.bookmybook.ui.adapters.OrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.item_buy_layout.*

class OrdersFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //To use option menu in fragment
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getOrderUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_orders, container, false)

        return root
    }

    private fun getOrderUI(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseFunctionsClass().getOrdersList(this@OrdersFragment)
    }

    fun successGetOrderUI(ordersList: ArrayList<Order>){
        hideProgressDialog()

        if(ordersList.size>0){
            rv_my_order_items.visibility=View.VISIBLE
            tv_no_orders_found.visibility=View.GONE

            rv_my_order_items.layoutManager = GridLayoutManager(activity,1)
            rv_my_order_items.setHasFixedSize(true)

            rv_my_order_items.adapter = OrdersListAdapter(requireActivity(),ordersList)
        }
        else{
            rv_my_order_items.visibility=View.GONE
            tv_no_orders_found.visibility=View.VISIBLE
        }
    }
}