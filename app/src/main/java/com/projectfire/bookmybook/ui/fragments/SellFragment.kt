package com.projectfire.bookmybook.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.firestore.FirestoreClass
import com.projectfire.bookmybook.models.Product
//import com.projectfire.bookmybook.ui.activities.AddProductActivity
import com.projectfire.bookmybook.ui.adapters.SellListAdapter
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //To use option menu in fragment
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sell_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_add_product -> {
                //startActivity(Intent(activity, AddProductActivity::class.java))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList((this))
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()

        if (productsList.size > 0) {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = SellListAdapter(requireActivity(), productsList, this)
            rv_my_product_items.adapter = adapterProducts
        } else {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
    }

    fun productDeleteSuccess() {
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        getProductListFromFireStore()
        //
    }

    private fun showAlertDialogToDeleteProduct(productID: String){
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(R.drawable.ic_alert_delete)

        builder.setPositiveButton(resources.getString(R.string.yes)){dialogInterface,_ ->
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().deleteProduct(this,productID)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.no)){dialogInterface,_ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun deleteProduct(productID: String) {
        showAlertDialogToDeleteProduct(productID)
    }
}
