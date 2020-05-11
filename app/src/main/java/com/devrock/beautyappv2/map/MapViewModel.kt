package com.devrock.beautyappv2.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.net.SalonListItem
import com.devrock.beautyappv2.net.SalonListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _salonList = MutableLiveData<List<SalonListItem>>()
    val salonList: LiveData<List<SalonListItem>>
        get() = _salonList

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    fun getSalonsList(lon: Double, lat: Double, limit: Int, offset: Int, order: String, session: String) {

        scope.launch {

            val response = BeautyApi.retrofitService.getSalonsList(
                session = session,
                latitude = lat,
                longitude = lon,
                limit = limit,
                offset = offset,
                order = order
            ).await()

           _status.value = response.info.status

            Log.i("SALONS", response.info.toString())
            Log.i("list", response.payload.toString())

            if(_status.value == "Ok"){
                _salonList.value = response.payload.list
            } else if (_status.value == "Error"){
                _errorText.value = response.payload.text
                Log.i("MAP ERR", response.payload.text)
            }

        }

    }

}