package com.projectfire.bookmybook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectfire.bookmybook.Constants
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.Product
import com.projectfire.bookmybook.ui.fragments.SellFragment
import kotlinx.android.synthetic.main.item_sell_layout.view.*

open class SellListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: SellFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_sell_layout, parent, false)
        )
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_publisher.text = model.publisher
            when (model.condition) {
                Constants.GOOD -> {
                    holder.itemView.tv_item_condition.text = "Good Condition"
                }
                Constants.BAD -> {
                    holder.itemView.tv_item_condition.text = "Bad Condition"
                }
                Constants.AVERAGE -> {
                    holder.itemView.tv_item_condition.text = "Average Condition"
                }
            }
            holder.itemView.tv_item_price.text = "₹${model.price}"
            if (model.isbn == "") {
                holder.itemView.tv_item_isbn.text = model.isbn
            } else {
                holder.itemView.tv_item_isbn.text = "ISBN: ${model.isbn}"
            }
            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }

            holder.itemView.setOnClickListener {
//                val intent = Intent(context,ProductDetailsActivity::class.java)
//                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
//                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
//                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}