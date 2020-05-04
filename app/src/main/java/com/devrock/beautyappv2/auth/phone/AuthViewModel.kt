package com.devrock.beautyappv2.auth.phone

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.util.phoneFormat
import kotlinx.coroutines.*

class AuthViewModel(): ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun setButtonAvailability(count: Int) : Boolean {
        return count >= 18
    }

    private var supervisorJob = SupervisorJob()



    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status



    fun sendCode(phone: String) {

        val phoneFormatted = phoneFormat(phone)

       scope.launch() {
            val status = BeautyApi.retrofitService.authSendCode(phoneFormatted).await()
            _status.value = status.info.status
            Log.i("STATUS", _status.value)
        }

    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("DSERROR", "An error happened: $e")
    }


}