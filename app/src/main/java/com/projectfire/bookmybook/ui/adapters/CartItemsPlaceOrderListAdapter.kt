package com.projectfire.bookmybook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.CartItem
import kotlinx.android.synthetic.main.item_cart_layout.view.*

open class CartItemsPlaceOrderListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_place_order_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_cart_item_image)
            holder.itemView.tv_cart_item_title.text = model.title
            holder.itemView.tv_cart_item_price.text = "₹${model.price}"
            holder.itemView.tv_cart_item_publisher.text = model.publisher
            holder.itemView.tv_cart_quantity.text = model.cart_quantity
            if (model.isbn == "") {
                holder.itemView.tv_cart_item_isbn.text = model.isbn
            } else {
                holder.itemView.tv_cart_item_isbn.text = "ISBN: ${model.isbn}"
            }

            if (model.cart_quantity == "0") {

                holder.itemView.tv_cart_quantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}