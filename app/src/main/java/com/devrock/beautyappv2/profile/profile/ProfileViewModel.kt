package com.devrock.beautyappv2.profile.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.AccountBody
import com.devrock.beautyappv2.net.AccountPayload
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.net.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _accountInfo = MutableLiveData<AccountPayload>()
    val accountInfo: LiveData<AccountPayload>
        get() = _accountInfo

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    fun getCurrentAccount(session: String) {

        scope.launch {

            val res = BeautyApi.retrofitService.getCurrentAccount(session).await()

            if (res.info.status == "Ok") {

                _accountInfo.value = res.payload

            } else if (res.info.status == "Error") {

                _errorText.value = res.payload.text

            }

        }

    }

    fun accountEdit(session: String?, name: String) {

        val parts = name.split(" ")

        val firstName = parts[0]
        val lastName = parts[1]

        val body = AccountBody(firstName, lastName)

        if (session != null) {

            scope.launch() {
                val status = BeautyApi.retrofitService.accountUpdate(session, body).await()
                _status.value = status.info.status
                Log.i("STATUS", _status.value)
            }

        } else Log.e("AccountCreation", "Error: Null Session")
    }

}