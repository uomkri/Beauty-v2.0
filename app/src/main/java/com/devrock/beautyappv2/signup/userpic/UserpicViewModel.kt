package com.devrock.beautyappv2.signup.userpic

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel

class UserpicViewModel : ViewModel() {

    fun getPath(applicationContext: Context, uri: Uri): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = applicationContext.getContentResolver().query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor!!.getColumnIndexOrThrow(proj[0])
                result = cursor!!.getString(columnIndex)
            }
            cursor!!.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }
}