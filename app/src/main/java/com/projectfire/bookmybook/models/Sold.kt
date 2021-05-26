package com.projectfire.bookmybook.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sold(
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",
    val order_id: String = "",
    val order_date: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val address: String ="",
    val pin: String ="",
    val name: String ="",
    val mobile: String ="",
    var id: String = "",
) : Parcelable