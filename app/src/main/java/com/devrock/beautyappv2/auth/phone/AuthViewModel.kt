package com.devrock.beautyappv2.auth.phone

import android.content.SharedPreferences
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

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private val _session = MutableLiveData<String>()
    val session: LiveData<String>
        get() = _session

    private val _isScopeActive = MutableLiveData<Boolean>()
    val isScopeActive: LiveData<Boolean>
        get() = _isScopeActive

    fun sendCode(phone: String) {

        val phoneFormatted = phoneFormat(phone)

       scope.launch() {
            val status = BeautyApi.retrofitService.authSendCode(phoneFormatted).await()
            _status.value = status.info.status
            Log.i("STATUS", _status.value)
           _isScopeActive.value = scope.isActive
        }

    }

    fun getLocalSession(prefs: SharedPreferences) {
        _session.value = prefs.getString("session", null)
    }

    fun checkLogin() {


        Log.e("SESSION", session.value.toString())

        val session = session.value

        if(session != null) {
            scope.launch {
                val response = BeautyApi.retrofitService.getCurrentAccount(session).await()
                _isLoggedIn.value = response.info.status == "Ok"
            }

        } else {
            _isLoggedIn.value = false
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e("DSERROR", "An error happened: $e")
    }


}