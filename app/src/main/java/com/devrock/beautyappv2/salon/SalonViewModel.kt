package com.devrock.beautyappv2.salon

import android.graphics.Point
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.*
import com.github.underscore.U
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SalonViewModel : ViewModel() {

    private val PHOTOS_ENDPOINT = "https://beauty.judoekb.ru/api/salons/photo/"

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

    private val _photosList = MutableLiveData<List<String>>()
    val photosList: LiveData<List<String>>
        get() = _photosList

    val screenSize = MutableLiveData<Point>()

    val photosGridList = MutableLiveData<MutableList<SalonPhoto>>()

    val session = MutableLiveData<String>()

    fun getSalonById(id: Int, lon: Double, lat: Double) {

        scope.launch {

            val response = BeautyApi.retrofitService.getSalonById(lon, lat, id).await()

            _status.value = response.info.status

            if(_status.value == "Ok"){
                _salonInfo.value = response.payload

                val photos = response.payload.photos
                _photosList.value = photos.map { PHOTOS_ENDPOINT + it }

            } else if (_status.value == "Error"){
                _errorText.value = response.payload.text
            }

        }

    }

    //formats schedule from an array of weekday objects with start and end times to an array of strings

    fun formatSchedule(list: List<ScheduleItem>): List<String> {

        //replace numbers with weekdays
        val formattedList = list.map {
            when (it.weekday) {
                1 -> FormattedWeekday("пн", it.startTime, it.endTime)
                2 -> FormattedWeekday("вт", it.startTime, it.endTime)
                3 -> FormattedWeekday("ср", it.startTime, it.endTime)
                4 -> FormattedWeekday("чт", it.startTime, it.endTime)
                5 -> FormattedWeekday("пт", it.startTime, it.endTime)
                6 -> FormattedWeekday("сб", it.startTime, it.endTime)
                7 -> FormattedWeekday("вс", it.startTime, it.endTime)
                else -> null
            }
        }

        //group days by equal start and end times
        val groups = U.groupBy(formattedList) {
            return@groupBy it!!.startTime + "#" + it.endTime
        }

        //convert to strings
        val data = U.map(groups.toList()) {
            if (it.second.size > 1){
                return@map FormattedWeekday1(
                    "${it.second[0]!!.weekday}-${U.last(it.second)!!.weekday}",
                    "с ${it.second[0]!!.startTime.slice(0..4)} до ${it.second[0]!!.endTime.slice(0..4)}"
                )
            } else {
                return@map FormattedWeekday1(
                    "${it.second[0]!!.weekday}",
                    "c ${it.second[0]!!.startTime.slice(0..4)} до ${it.second[0]!!.endTime.slice(0..4)}"
                )
            }
        }

        val result = U.map(data) {
            return@map "${it.date} ${it.time}"
        }

       return result

    }

}