package com.devrock.beautyappv2.signup.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    private var _isFinalPage = MutableLiveData<Boolean>()
    val isFinalPage : LiveData<Boolean>
        get() = _isFinalPage

    fun checkFinalPage(num: Int) {
        _isFinalPage.value = num == 2
    }

}