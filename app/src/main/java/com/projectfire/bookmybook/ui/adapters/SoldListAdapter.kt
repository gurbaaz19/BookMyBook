package com.projectfire.bookmybook.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectfire.bookmybook.Constants
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Order
import com.projectfire.bookmybook.models.Sold
import com.projectfire.bookmybook.ui.activities.OrderDetailsActivity
import kotlinx.android.synthetic.main.item_buy_layout.view.*

open class SoldListAdapter(
    private val context: Context,
    private var list: ArrayList<Sold>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_buy_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_dashboard_item_image
            )

            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_publisher.visibility=View.GONE
            holder.itemView.tv_dashboard_item_price.text = "₹${model.price}"

//            holder.itemView.setOnClickListener {
//                val intent = Intent(context, OrderDetailsActivity::class.java)
//                intent.putExtra(Constants.EXTRA_ORDER_DETAILS, model)
//                context.startActivity(intent)
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
