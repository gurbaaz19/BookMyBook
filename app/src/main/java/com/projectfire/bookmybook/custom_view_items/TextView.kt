package com.projectfire.bookmybook.custom_view_items

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextViewCustomRegular(context: Context, attributeSet: AttributeSet): AppCompatTextView(context,attributeSet) {

    init{
        applyFont()
    }

    private fun applyFont() {
       val regularTypeface: Typeface =
           Typeface.createFromAsset(context.assets,"Mont-R.ttf")
        typeface= regularTypeface
    }
}

class TextViewCustomBold(context: Context, attributeSet: AttributeSet): AppCompatTextView(context,attributeSet) {

    init{
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets,"Mont-B.ttf")
        typeface= boldTypeface
    }
}