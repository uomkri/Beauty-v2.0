package com.devrock.beautyappv2.profile.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpViewModel : ViewModel() {

    private val _q1IsOpen = MutableLiveData<Boolean>()
    val q1isOpen : LiveData<Boolean>
        get() = _q1IsOpen

    private val _q2IsOpen = MutableLiveData<Boolean>()
    val q2isOpen : LiveData<Boolean>
        get() = _q2IsOpen

    fun initQs() {

        _q1IsOpen.value = false
        _q2IsOpen.value = false

    }

    fun q1ChangeStatus() {

        _q1IsOpen.value = !_q1IsOpen.value!!

    }

    fun q2ChangeStatus() {

        _q2IsOpen.value = !_q2IsOpen.value!!

    }

}