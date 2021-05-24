package com.projectfire.bookmybook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.projectfire.bookmybook.Constants
import com.projectfire.bookmybook.FirebaseFunctionsClass
import com.projectfire.bookmybook.GlideLoader
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.CartItem
import com.projectfire.bookmybook.ui.activities.CartListActivity
import kotlinx.android.synthetic.main.item_cart_layout.view.*
import kotlinx.android.synthetic.main.item_sell_layout.view.*

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
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
            holder.itemView.tv_cart_item_publisher.text= model.publisher
            holder.itemView.tv_cart_quantity.text = model.cart_quantity
            if (model.isbn == "") {
                holder.itemView.tv_cart_item_isbn.text = model.isbn
            } else {
                holder.itemView.tv_cart_item_isbn.text = "ISBN: ${model.isbn}"
            }

            if (model.cart_quantity == "0") {
                holder.itemView.ib_remove_cart_item.visibility = View.GONE
                holder.itemView.ib_add_cart_item.visibility = View.GONE

                holder.itemView.tv_cart_quantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                holder.itemView.ib_remove_cart_item.visibility = View.VISIBLE
                holder.itemView.ib_add_cart_item.visibility = View.VISIBLE

                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }

            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when (context) {
                    is CartListActivity -> {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirebaseFunctionsClass().removeItemFromCart(context, model.id)
            }

            holder.itemView.ib_remove_cart_item.setOnClickListener {
                if (model.cart_quantity == "1") {
                    FirebaseFunctionsClass().removeItemFromCart(context, model.id)
                } else {
                    val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    when (context) {
                        is CartListActivity -> {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        }
                    }

                    FirebaseFunctionsClass().updateMyCart(context, model.id, itemHashMap)
                }
            }

            holder.itemView.ib_add_cart_item.setOnClickListener {

                val cartQuantity: Int = model.cart_quantity.toInt()
                if (cartQuantity < model.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    when (context) {
                        is CartListActivity -> {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        }
                    }

                    FirebaseFunctionsClass().updateMyCart(context, model.id, itemHashMap)
                } else {
                    when (context) {
                        is CartListActivity -> {
                            context.showSnackBar(
                                context.resources.getString(
                                    R.string.msg_for_available_stock,
                                    model.stock_quantity
                                ), true
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}