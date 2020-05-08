package com.devrock.beautyappv2.signup.userpic

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class UserpicViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
            val status: LiveData<String>
                get() = _status

    private val _img = MutableLiveData<File>()
            val img: LiveData<File>
                get() = _img



    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

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

    fun accountSetPhoto(data: String?, session: String) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = session
        headers["Content-Type"] = "image/png"

        scope.launch {
            val status = BeautyApi.retrofitService.accountSetPhoto(headers, data!!).await()
            _status.value = status.info.status
        }
    }

    fun getCompressedImageAsync(image: File, context: Context) {

        scope.launch {
            val compressedImage = Compressor.compress(context, image) {
                format(Bitmap.CompressFormat.JPEG)
            }
            _img.value = compressedImage
        }
    }


}