package com.devrock.beautyappv2.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun phoneFormat(phone: String) : String {
    var re = Regex("[^0-9 ]")
    var phoneFormatted = re.replace(phone, "")
    re = Regex("\\s")
    phoneFormatted = re.replace(phoneFormatted, "")
    phoneFormatted = "+$phoneFormatted"
    return phoneFormatted
}

fun getBitmapFromVectorDrawable (context: Context, drawableId: Int) : Bitmap {
    var drawable = ContextCompat.getDrawable(context, drawableId)

    val bitmap : Bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas : Canvas = Canvas(bitmap)
    drawable!!.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

class BeautyUtils {



}



