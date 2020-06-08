package com.devrock.beautyappv2.splash

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _session = MutableLiveData<String>()
    val session: LiveData<String>
        get() = _session

    private val _isScopeActive = MutableLiveData<Boolean>()
    val isScopeActive: LiveData<Boolean>
        get() = _isScopeActive

    fun getLocalSession(prefs: SharedPreferences) {
        _session.value = prefs.getString("session", null)
    }

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

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

}