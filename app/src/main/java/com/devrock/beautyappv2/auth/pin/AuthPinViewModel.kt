package com.devrock.beautyappv2.auth.pin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.AuthConfirmBody
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.util.Prefs
import com.devrock.beautyappv2.util.phoneFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthPinViewModel() : ViewModel() {

    var phone: String = ""

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _session = MutableLiveData<String>()
    val session: LiveData<String>
        get() = _session

    private val _registered = MutableLiveData<Boolean>()
    val registered: LiveData<Boolean>
        get() = _registered

    var prefs: Prefs? = null

    fun phoneConfirm(phone: String, code: String) {

        val phoneFormatted = phoneFormat(phone)

        var body = AuthConfirmBody(phoneFormatted, code)

        scope.launch() {
            val response = BeautyApi.retrofitService.authPhoneConfirm(body).await()
            _status.value = response.info.status
            _session.value = response.payload.session
            _registered.value = response.payload.registered
            Log.i("Session", _session.value)
        }

        prefs!!.session = _session.value

    }

}