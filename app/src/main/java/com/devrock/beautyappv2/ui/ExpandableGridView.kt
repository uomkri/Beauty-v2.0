package com.devrock.beautyappv2.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView
import androidx.constraintlayout.widget.ConstraintLayout

class ExpandableGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GridView(context, attrs) {

    var expanded = true

    fun isExpanded() : Boolean {
        return expanded
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isExpanded()) {
            val expandSpec = MeasureSpec.makeMeasureSpec(View.MEASURED_SIZE_MASK, MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, expandSpec)

            val params = layoutParams as ConstraintLayout.LayoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

    }

}