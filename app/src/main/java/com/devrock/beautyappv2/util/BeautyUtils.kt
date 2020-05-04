package com.devrock.beautyappv2.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.devrock.beautyappv2.signup.name.PERMISSION_REQUEST_CODE
import java.io.File

fun phoneFormat(phone: String) : String {
    var re = Regex("[^0-9 ]")
    var phoneFormatted = re.replace(phone, "")
    re = Regex("\\s")
    phoneFormatted = re.replace(phoneFormatted, "")
    phoneFormatted = "+$phoneFormatted"
    return phoneFormatted
}

@RequiresApi(Build.VERSION_CODES.O)
fun encodeBase64(path: String): String {
    //val bytes = File(path).readBytes()
    //return Base64.encodeToString(bytes, Base64.DEFAULT)
    return java.util.Base64.getMimeEncoder().encodeToString(File(path).readBytes())
}

class Prefs (context: Context?) {
    val FILENAME = "com.devrock.beautyappv2.prefs"
    val SESSION = "session"
    val prefs: SharedPreferences? = context?.getSharedPreferences(FILENAME, 0)
    var session: String?
        get() = prefs?.getString(SESSION, "")
        set(value) = prefs?.edit()?.putString(SESSION, value)?.apply()!!
}



