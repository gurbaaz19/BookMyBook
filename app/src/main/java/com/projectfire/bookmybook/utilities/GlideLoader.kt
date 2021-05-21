package com.projectfire.bookmybook.utilities

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.projectfire.bookmybook.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            //load the image in the image viewer
            Glide
                .with(context)
                .load(image) // URI of the image
                .centerCrop() // Scale type of the image
                .placeholder(R.drawable.ic_user_placeholder) //Default in case of failure
                .into(imageView) // the view in which image is loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            //load the image in the image viewer
            Glide
                .with(context)
                .load(image) // URI of the image
                .centerCrop() // Scale type of the image
                .into(imageView) // the view in which image is loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}