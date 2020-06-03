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
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 69,124, false)
    val canvas : Canvas = Canvas(bitmap)
    drawable!!.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}



    fun getFormattedMonth(month: String): String {

        var format: String = ""

        when (month) {
            "01" -> format = "января"
            "02" -> format = "февраля"
            "03" -> format = "марта"
            "04" -> format = "апреля"
            "05" -> format = "мая"
            "06" -> format = "июня"
            "07" -> format = "июля"
            "08" -> format = "августа"
            "09" -> format = "сентября"
            "10" -> format = "октября"
            "11" -> format = "ноября"
            "12" -> format = "декабря"
        }

        return format
    }



