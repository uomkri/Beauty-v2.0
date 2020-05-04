package com.devrock.beautyappv2.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import com.devrock.beautyappv2.R

class AvatarEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttrs) {

    private var initials: String = "??"
    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private lateinit var resultBm : Bitmap
    private lateinit var maskBm : Bitmap
    private lateinit var srcBm : Bitmap

    init {
        if(attrs != null){
            val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarEditView)
            initials = ta.getString(R.styleable.AvatarEditView_aev_ititials) ?: "??"
        }

        scaleType = ScaleType.CENTER_CROP
        setup()

    }

    private fun setup() {
        with(maskPaint) {
            color = Color.RED
            style = Paint.Style.FILL
        }
    }

    private fun prepareBitmaps(w: Int, h: Int) {

        maskBm = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
        resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)
        val maskCanvas = Canvas(maskBm)
        maskCanvas.drawOval(viewRect.toRectF(), maskPaint)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)

        val resultCanvas = Canvas(resultBm)
        resultCanvas.drawBitmap(maskBm, viewRect, viewRect, null)
        resultCanvas.drawBitmap(srcBm, viewRect, viewRect, maskPaint)

    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        canvas.drawBitmap(resultBm, viewRect, viewRect, null)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //super.onSizeChanged(w, h, oldw, oldh)
        if(w==0){
            with(viewRect){
                left = 0
                top = 0
                right = w
                bottom = h
            }

            prepareBitmaps(w, h)
        }
    }

}