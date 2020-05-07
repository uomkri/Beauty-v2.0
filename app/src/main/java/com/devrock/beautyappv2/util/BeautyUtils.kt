package com.devrock.beautyappv2.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import androidx.annotation.RequiresApi
import com.devrock.beautyappv2.signup.userpic.PERMISSION_REQUEST_CODE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


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
    //val data = Base64.encodeToString(bytes, Base64.URL_SAFE)
    //Log.e("PATH", path)
    //Log.e("BASE64", data)
    //return java.util.Base64.getMimeEncoder().encode(File(path).readBytes()).toString()
    val bm: Bitmap = BitmapFactory.decodeFile(path)
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val bytes = baos.toByteArray()
    val data = Base64.encodeToString(bytes, Base64.DEFAULT)
    Log.e("BASE64", data)
    /*val base64 = FileInputStream(path).use { inputStream ->
        ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.NO_WRAP). use { base64FilterStream ->
                inputStream.copyTo(base64FilterStream)
                base64FilterStream.flush()
                outputStream.toString()
            }
        }
    }*/

    return "pisos"
}

class BeautyUtils {

}



