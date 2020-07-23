package com.devrock.beautyappv2.rent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.net.MasterEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RentSuccessViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _latestEntry = MutableLiveData<MasterEntry>()
    val latestEntry: LiveData<MasterEntry>
        get() = _latestEntry

    fun getLatestEntry(session: String) {

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, listOf("Created")).await()

            _latestEntry.value = res.payload.maxBy { it.id }

            Log.e("latest", _latestEntry.value.toString())

        }

    }

}