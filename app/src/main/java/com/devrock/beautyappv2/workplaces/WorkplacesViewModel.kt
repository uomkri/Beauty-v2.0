package com.devrock.beautyappv2.workplaces

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

class WorkplacesViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _createdMasterEntries = MutableLiveData<List<MasterEntry>>()
    val createdMasterEntries: LiveData<List<MasterEntry>>
        get() = _createdMasterEntries

    fun getMasterCurrentEntries(session: String) {

        val statuses = listOf("ConfirmedByMaster")

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, statuses)

        }

    }

    fun getCreatedMasterEntries(session: String) {

        val statuses = listOf("Created", "Declined")

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, statuses).await()

            _createdMasterEntries.value = res.payload.filter {
                it.rentType == "ByHours"
            }

            Log.i("res", _createdMasterEntries.value.toString())
        }

    }

}