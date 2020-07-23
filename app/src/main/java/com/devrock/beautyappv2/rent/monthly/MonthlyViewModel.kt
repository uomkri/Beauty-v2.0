package com.devrock.beautyappv2.rent.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devrock.beautyappv2.net.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MonthlyViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>>
        get() = _offers

    private var _offerDaysArray = MutableLiveData<MutableList<OfferDays>>()
    val offerDaysArray : LiveData<MutableList<OfferDays>>
        get() = _offerDaysArray

    private var _offerDay = MutableLiveData<OfferDaysResponse>()
    val offerDay : LiveData<OfferDaysResponse>
        get() = _offerDay

    private var _status = MutableLiveData<String>()
    val status : LiveData<String>
        get() = _status

    val selectedDate = MutableLiveData<String>()

    fun getOfferDays(id: Int, startDate: String) {

        scope.launch {

            val res = BeautyApi.retrofitService.getOfferDays(id, startDate).await()

            if (res.info.status == "Ok") {

                _offerDay.value = res

            }
        }

    }

    fun addOfferDay(offerDay: OfferDays) {

        _offerDaysArray.value!!.add(offerDay)

        _offerDaysArray.value = _offerDaysArray.value

    }

    fun addMonthEntry(session: String, startDate: String, offerId: Int) {

        scope.launch {

            val res = BeautyApi.retrofitService.addMonthEntry(session = session, startDate = startDate, offerId = offerId).await()

            _status.value = res.info.status

        }

    }

    fun getOffers() {

        scope.launch {

            val res = BeautyApi.retrofitService.getWorkplaceOffers(2).await()

            if (res.info.status == "Ok") {

                _offers.value = res.payload

            }

        }

    }

}