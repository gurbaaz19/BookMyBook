package com.projectfire.bookmybook.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: String = "",
    val pin: String = "",
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val service_charge: String = "",
    val total_amount: String = "",
    val time: Long = 0L,
    var id: String = ""
) : Parcelable