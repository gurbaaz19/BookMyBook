package com.projectfire.bookmybook.custom_view_items

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class ButtonCustomRegular(context: Context, attrs: AttributeSet) :
    AppCompatButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-R.ttf")
        typeface = regularTypeface
    }
}

class ButtonCustomBold(context: Context, attrs: AttributeSet) :
    AppCompatButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-B.ttf")
        typeface = boldTypeface
    }
}