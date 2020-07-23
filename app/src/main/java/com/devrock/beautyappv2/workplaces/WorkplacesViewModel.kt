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
import okhttp3.internal.format

class WorkplacesViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _createdMasterEntries = MutableLiveData<List<MasterEntry>>()
    val createdMasterEntries: LiveData<List<MasterEntry>>
        get() = _createdMasterEntries

    private val _currentMasterEntries = MutableLiveData<List<MasterEntry>>()
    val currentMasterEntries: LiveData<List<MasterEntry>>
        get() = _currentMasterEntries

    private val _pendingMasterEntries = MutableLiveData<List<MasterEntry>>()
    val pendingMasterEntries: LiveData<List<MasterEntry>>
        get() = _pendingMasterEntries

    private val _postStatus = MutableLiveData<String>()
    val postStatus: LiveData<String>
        get() = _postStatus

    fun getCurrentMasterEntries(session: String) {

        _currentMasterEntries.value = null

        val statuses = listOf("ConfirmedByMaster")

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, statuses).await()

            _currentMasterEntries.value = res.payload.filter { it.rentType == "ByHours" }

        }

    }

    fun statusClear() {
        _postStatus.value = null
    }

    fun getPendingMasterEntries(session: String) {

        _pendingMasterEntries.value = null

        val statuses = listOf("ConfirmedBySalon")

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, statuses).await()

            _pendingMasterEntries.value = res.payload.filter { it.rentType == "ByHours"}

        }

    }

    fun getCreatedMasterEntries(session: String) {

        _createdMasterEntries.value = null

        val statuses = listOf("Created", "Declined")

        scope.launch {

            val res = BeautyApi.retrofitService.getMasterEntries(session, statuses).await()

            _createdMasterEntries.value = res.payload.filter {
                it.rentType == "ByHours"
            }

            Log.i("res", _createdMasterEntries.value.toString())
        }

    }

    fun getFormattedEntryStatus(st: String) : String {

        var formatSt = ""

        when(st) {
            "Created" -> formatSt = "Ожидается подтверждение салоном"
            "Declined" -> formatSt = "Отказ"
        }

        return formatSt
    }

    fun entryConfirm(session: String, entryId: Int) {

        scope.launch {

            val res = BeautyApi.retrofitService.entryConfirm(session, entryId).await()

            _postStatus.value = res.info.status

        }

    }

    fun entryCancel(session: String, entryId: Int) {

        scope.launch {

            val res = BeautyApi.retrofitService.entryCancel(session, entryId).await()

            _postStatus.value = res.info.status

        }

    }

}