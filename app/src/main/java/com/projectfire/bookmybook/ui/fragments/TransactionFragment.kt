package com.projectfire.bookmybook.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import com.projectfire.bookmybook.models.Sold
import com.projectfire.bookmybook.ui.adapters.OrdersListAdapter
import com.projectfire.bookmybook.ui.adapters.SoldListAdapter
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.item_buy_layout.*

class TransactionFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //To use option menu in fragment
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getUI()
    }

    private fun getUI() {
        showProgressDialog(resources.getString(R.string.please_wait))
        getOrderUI()
        getSoldUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        return root
    }

    private fun getOrderUI(){
        FirebaseFunctionsClass().getOrdersList(this@TransactionFragment)
    }

    fun successGetOrderUI(ordersList: ArrayList<Order>){
        successGetUI()

        if(ordersList.size>0){
            ll_orders.visibility=View.VISIBLE
            rv_my_order_items.visibility=View.VISIBLE
            tv_no_orders_found.visibility=View.GONE
            tv_order_title.visibility=View.VISIBLE


            rv_my_order_items.layoutManager = GridLayoutManager(activity,1,LinearLayoutManager.HORIZONTAL,false)
            rv_my_order_items.setHasFixedSize(true)

            rv_my_order_items.adapter = OrdersListAdapter(requireActivity(),ordersList)
        }
        else{
            ll_orders.visibility=View.GONE
            rv_my_order_items.visibility=View.GONE
            tv_no_orders_found.visibility=View.VISIBLE
            tv_order_title.visibility=View.GONE
        }
    }

    private fun getSoldUI(){
        FirebaseFunctionsClass().getSoldList(this@TransactionFragment)
    }

    fun successGetSoldUI(soldList: ArrayList<Sold>){
        successGetUI()

        if(soldList.size>0){
            ll_sold.visibility=View.VISIBLE
            rv_my_sold_items.visibility=View.VISIBLE
            tv_no_sold_found.visibility=View.GONE
            tv_sold_title.visibility=View.VISIBLE


            rv_my_sold_items.layoutManager = GridLayoutManager(activity,1,LinearLayoutManager.HORIZONTAL,false)
            rv_my_sold_items.setHasFixedSize(true)

            rv_my_sold_items.adapter = SoldListAdapter(requireActivity(),soldList)
        }
        else{
            ll_sold.visibility=View.GONE
            rv_my_sold_items.visibility=View.GONE
            tv_no_sold_found.visibility=View.VISIBLE
            tv_sold_title.visibility=View.GONE
        }
    }

    private fun successGetUI() {
        hideProgressDialog()
    }
}