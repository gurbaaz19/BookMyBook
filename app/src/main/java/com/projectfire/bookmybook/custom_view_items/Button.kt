package com.projectfire.bookmybook.custom_view_items

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class ButtonMontserratRegular(context: Context, attrs: AttributeSet) :
    AppCompatButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = regularTypeface
    }
}

class ButtonMontserratBold(context: Context, attrs: AttributeSet) :
    AppCompatButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeface
    }
}