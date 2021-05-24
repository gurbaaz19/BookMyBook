package com.projectfire.bookmybook.custom_view_items

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditTextCustomRegular(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-R.ttf")
        typeface = regularTypeface
    }
}

class EditTextCustomBold(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Mont-B.ttf")
        typeface = boldTypeface
    }
}