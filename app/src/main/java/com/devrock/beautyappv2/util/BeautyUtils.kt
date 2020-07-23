package com.devrock.beautyappv2.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.devrock.beautyappv2.net.TimeSlot

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

fun formatTimeslots(slots: List<TimeSlot>): String {

    val formatted = slots.map {
        return@map "${it.start?.dropLast(3)}-${it.end?.dropLast(3)}"
    }

    return formatted.toString().drop(1).dropLast(1)

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

    fun getFormattedDate(date: String): String {

        var formattedMonth: String = ""

        val day = date.substring(8, 10)
        val month = date.substring(5, 7)
        val year = date.substring(0, 4)

        when (month) {
            "01" -> formattedMonth = "января"
            "02" -> formattedMonth = "февраля"
            "03" -> formattedMonth = "марта"
            "04" -> formattedMonth = "апреля"
            "05" -> formattedMonth = "мая"
            "06" -> formattedMonth = "июня"
            "07" -> formattedMonth = "июля"
            "08" -> formattedMonth = "августа"
            "09" -> formattedMonth = "сентября"
            "10" -> formattedMonth = "октября"
            "11" -> formattedMonth = "ноября"
            "12" -> formattedMonth = "декабря"
        }

        return "$day $formattedMonth $year"

    }



