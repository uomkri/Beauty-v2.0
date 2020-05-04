package com.devrock.beautyappv2.signup.name

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.AccountBody
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.util.encodeBase64

import kotlinx.coroutines.*

class NameViewModel : ViewModel() {

    fun setButtonAvailability(nameLength: Int, lastNameLength: Int): Boolean {
        return nameLength > 0 && lastNameLength > 0
    }

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var supervisorJob = SupervisorJob()



    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("DSERROR", "An error happened: $e")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun accountCreate(session: String?, firstName: String, lastName: String, imgPath: String?) {

        val body = AccountBody(firstName, lastName)

        if(session != null) {

        scope.launch() {
            val status = BeautyApi.retrofitService.accountUpdate(session, body).await()
            _status.value = status.info.status
            Log.i("STATUS", _status.value)
        }

        if( imgPath != null) {



            val base64 = encodeBase64(imgPath)
           // val base64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAABhGlDQ1BJQ0MgcHJvZmlsZQAAKJF9kT1Iw0AcxV9TRZGKQyuIOGSoThZFRRy1CkWoEGqFVh1MLv2CJg1Jiouj4Fpw8GOx6uDirKuDqyAIfoA4OTopukiJ/0sKLWI8OO7Hu3uPu3eAUC8zzeoYBzTdNlOJuJjJropdrxDQjzDGEJKZZcxJUhK+4+seAb7exXiW/7k/R6+asxgQEIlnmWHaxBvE05u2wXmfOMKKskp8Tjxq0gWJH7muePzGueCywDMjZjo1TxwhFgttrLQxK5oa8RRxVNV0yhcyHquctzhr5Spr3pO/MJTTV5a5TnMICSxiCRJEKKiihDJsxGjVSbGQov24j3/Q9UvkUshVAiPHAirQILt+8D/43a2Vn5zwkkJxoPPFcT6Gga5doFFznO9jx2mcAMFn4Epv+St1YOaT9FpLix4BfdvAxXVLU/aAyx1g4MmQTdmVgjSFfB54P6NvygLhW6BnzeutuY/TByBNXSVvgINDYKRA2es+7+5u7+3fM83+fgBmqHKidBk7uAAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAAd0SU1FB+QCFRMwGI5MtHUAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAAC0lEQVQI12NgAAIAAAUAAeImBZsAAAAASUVORK5CYII="
            val headers = HashMap<String, String>()
            headers["Authorization"] = session
            headers["Content-Type"] = "image/png"

            scope.launch {
                val status = BeautyApi.retrofitService.accountSetPhoto(headers, base64).await()
                _status.value = status.info.status
            }
        }
        } else Log.e("AccountCreation", "Error: Null Session")
    }

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