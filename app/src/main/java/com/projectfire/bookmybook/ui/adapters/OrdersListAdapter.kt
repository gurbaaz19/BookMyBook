package com.projectfire.bookmybook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import kotlinx.android.synthetic.main.item_buy_layout.view.*

open class OrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewOrdersHolder(
            LayoutInflater.from(context).inflate(R.layout.item_buy_layout, parent, false)
        )
    }

    class MyViewOrdersHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

            if (holder is MyViewOrdersHolder) {
//                GlideLoader(context).loadProductPicture(
//                    model.image,
//                    holder.itemView.iv_dashboard_item_image
//                )
//                holder.itemView.tv_dashboard_item_title.text = model.title
//                holder.itemView.tv_dashboard_item_price.text = "₹${model.total_amount}"

            }
        }
    override fun getItemCount(): Int {
        return list.size
    }
}