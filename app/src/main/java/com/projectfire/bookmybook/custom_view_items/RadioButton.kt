package com.projectfire.bookmybook.custom_view_items

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RadioButtonCustomRegular(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-R.ttf")
        typeface = regularTypeface
    }
}

class RadioButtonCustomBold(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-B.ttf")
        typeface = boldTypeface
    }
}