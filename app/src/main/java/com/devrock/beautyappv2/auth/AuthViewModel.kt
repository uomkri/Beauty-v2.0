package com.devrock.beautyappv2.auth

import android.util.Log
import androidx.lifecycle.ViewModel
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

    private var _status: String = ""

    fun sendCode(phone: String) {

        val phoneFormatted = phoneFormat(phone)

       scope.launch(getJobErrorHandler() + supervisorJob) {
           TODO("Разобраться с java.lang.ExceptionInInitializerError")
            val status = BeautyApi.retrofitService.sendCode(phoneFormatted).await()
            _status = status.status
            Log.i("STATUS", status.toString())
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("DSERROR", "An error happened: $e")
    }


}