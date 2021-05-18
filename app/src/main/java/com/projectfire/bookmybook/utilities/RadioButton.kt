package com.projectfire.bookmybook.utilities

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RadioButtonMontserratRegular(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = regularTypeface
    }
}

class RadioButtonMontserratBold(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeface
    }
}