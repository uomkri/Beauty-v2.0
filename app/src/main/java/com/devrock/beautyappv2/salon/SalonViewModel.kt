package com.devrock.beautyappv2.salon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.net.SalonByIdPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SalonViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    private val _salonInfo = MutableLiveData<SalonByIdPayload>()
    val salonInfo: LiveData<SalonByIdPayload>
        get() = _salonInfo

    fun getSalonById(id: Int, lon: Double, lat: Double) {

        scope.launch {

            val response = BeautyApi.retrofitService.getSalonById(lon, lat, id).await()

            _status.value = response.info.status

            if(_status.value == "Ok"){
                _salonInfo.value = response.payload
            } else if (_status.value == "Error"){
                _errorText.value = response.payload.text
            }

        }

    }

}