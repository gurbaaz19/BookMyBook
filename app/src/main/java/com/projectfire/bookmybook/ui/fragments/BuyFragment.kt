package com.projectfire.bookmybook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.models.Product
import com.projectfire.bookmybook.ui.activities.CartListActivity
//import com.projectfire.bookmybook.ui.activities.ProductDetailsActivity
import com.projectfire.bookmybook.ui.activities.SettingsActivity
import com.projectfire.bookmybook.ui.adapters.BuyListAdapter
import kotlinx.android.synthetic.main.fragment_buy.*

class BuyFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //To use option menu in fragment
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_buy, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.buy_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))

                return true
            }

            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {
        hideProgressDialog()
        if (dashboardItemsList.size > 0) {
            rv_my_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_my_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_my_dashboard_items.setHasFixedSize(true)
            val adapter = BuyListAdapter(requireActivity(), dashboardItemsList)
            rv_my_dashboard_items.adapter = adapter
        } else {
            rv_my_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseFunctionsClass().getDashboardItemsList(this@BuyFragment)
    }
}