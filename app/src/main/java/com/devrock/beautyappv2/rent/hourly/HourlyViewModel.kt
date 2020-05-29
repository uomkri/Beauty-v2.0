package com.devrock.beautyappv2.rent.hourly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.BeautyApi
import com.devrock.beautyappv2.net.HourPrice
import com.devrock.beautyappv2.net.TimeSlot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Time

class HourlyViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val selectedDate = MutableLiveData<String>()

    private val _timeslotsRaw = MutableLiveData<List<TimeSlot>>()
    val timeslotsRaw: LiveData<List<TimeSlot>>
        get() = _timeslotsRaw

    private val _hourPrices = MutableLiveData<List<HourPrice>>()
    val hourPrices: LiveData<List<HourPrice>>
        get() = _hourPrices

    private val _selectedTimeslots = MutableLiveData<MutableList<TimeSlot?>>()
    val selectedTimeslots: LiveData<MutableList<TimeSlot?>>
        get() = _selectedTimeslots



    val timeslotsMorning: MutableList<TimeSlot?> = mutableListOf(null)

    val timeslotsDay: MutableList<TimeSlot?> = mutableListOf(null)

    val timeslotsEvening: MutableList<TimeSlot?> = mutableListOf(null)

    //val selectedTimeslots = mutableListOf<TimeSlot?>(null)

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    fun getTimeslots(workplaceId: Int, date: String) {

        _selectedTimeslots.value = mutableListOf(null)

        scope.launch {

            val response = BeautyApi.retrofitService.getWorkplaceTimeslots(date, workplaceId).await()

            if (response.info.status == "Ok") {
                _timeslotsRaw.value = response.payload
            }
        }

    }

    fun addTimeslot(timeSlot: TimeSlot) {
        _selectedTimeslots.value!!.add(timeSlot)
        _selectedTimeslots.value = _selectedTimeslots.value!!
    }

    fun removeTimeslot(timeSlot: TimeSlot) {
        _selectedTimeslots.value!!.remove(timeSlot)
        _selectedTimeslots.value = _selectedTimeslots.value!!
    }



    fun getHourPrices(workplaceId: Int) {

        scope.launch {

            val response = BeautyApi.retrofitService.getWorkplaceHourPrices(workplaceId).await()

            if (response.info.status == "Ok") {
                _hourPrices.value = response.payload
            }
        }

    }

    fun sortTimeslots(list: List<TimeSlot>) {

        timeslotsMorning.clear()
        timeslotsDay.clear()
        timeslotsEvening.clear()

        for (item in list) {

            val startTime = item.start!!.removeRange(2, item.start!!.length).toInt()

            when {
                startTime < 12 -> timeslotsMorning.add(item)
                startTime in 12..17 -> timeslotsDay.add(item)
                startTime >= 18 -> timeslotsEvening.add(item)
            }

            Log.i("morning", timeslotsMorning.toString())
            Log.i("day", timeslotsDay.toString())
            Log.i("ev", timeslotsEvening.toString())
        }

    }
}