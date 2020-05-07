package com.devrock.beautyappv2.auth.pin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.AuthConfirmBody
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.util.phoneFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.os.CountDownTimer

class AuthPinViewModel() : ViewModel() {

    companion object {
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 120000L
    }

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime


    init {
        timer = object : CountDownTimer(COUNTDOWN_TIME ,  ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND )
            }
            override fun onFinish() {
                _currentTime.value = DONE
            }
        }
        timer.start()
    }

    fun startTimer() {
        timer.start()
    }


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

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText



    fun phoneConfirm(phone: String, code: String) {

        val phoneFormatted = phoneFormat(phone)

        var body = AuthConfirmBody(phoneFormatted, code)

        scope.launch() {
            val response = BeautyApi.retrofitService.authPhoneConfirm(body).await()
            _status.value = response.info.status
            if(_status.value == "Ok"){
                _session.value = response.payload.session
                _registered.value = response.payload.registered
            } else if (_status.value == "Error"){
                _errorText.value = response.payload.text
            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

}

