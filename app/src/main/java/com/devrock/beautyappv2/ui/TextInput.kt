package com.devrock.beautyappv2.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import com.devrock.beautyappv2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ColorTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : TextInputLayout(context, attrs, defStyleAttrs) {

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(focused) this.boxStrokeColor = context.getColor(R.color.tinput_hasfocus) else this.boxStrokeColor = context.getColor(R.color.tinput_nofocus)
        Log.i("focus", "focus")
    }
}