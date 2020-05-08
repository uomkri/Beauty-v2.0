package com.devrock.beautyappv2.signup.name


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.AccountBody
import com.devrock.beautyappv2.net.BeautyApi

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

    fun accountCreate(session: String?, firstName: String, lastName: String) {

        val body = AccountBody(firstName, lastName)

        if(session != null) {

        scope.launch() {
            val status = BeautyApi.retrofitService.accountUpdate(session, body).await()
            _status.value = status.info.status
            Log.i("STATUS", _status.value)
        }

        } else Log.e("AccountCreation", "Error: Null Session")
    }




}