package com.devrock.beautyappv2.util

import android.util.Log

fun phoneFormat(phone: String) : String {
    var re = Regex("[^0-9 ]")
    var phoneFormatted = re.replace(phone, "")
    re = Regex("\\s")
    phoneFormatted = re.replace(phoneFormatted, "")
    phoneFormatted = "+$phoneFormatted"
    Log.i("PHONE", phoneFormatted)
    return phoneFormatted
}