package com.devrock.beautyappv2.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.*
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

    private val _hourPrices = MutableLiveData<List<OfferTimeslot>>()
    val hourPrices: LiveData<List<OfferTimeslot>>
        get() = _hourPrices

    private val _salonWorkplaces = MutableLiveData<List<Workplace>>()
    val salonWorkplaces: LiveData<List<Workplace>>
        get() = _salonWorkplaces

    private val _salonInfo = MutableLiveData<SalonByIdPayload>()
    val salonInfo: LiveData<SalonByIdPayload>
        get() = _salonInfo

    private val _userLon = MutableLiveData<Double>()
    val userLon: LiveData<Double>
        get() = _userLon

    private val _userLat = MutableLiveData<Double>()
    val userLat: LiveData<Double>
        get() = _userLat

    fun updateDeviceLocation(lon: Double, lat: Double) {

        _userLon.value = lon
        _userLat.value = lat

    }



    fun getSalonWorkplaces(session: String, salonId: Int) {

        scope.launch {

            val response = BeautyApi.retrofitService.getSalonWorkplaces(session, salonId).await()

            if (response.info.status == "Ok") {
                _salonWorkplaces.value = response.payload
            }

        }

    }

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