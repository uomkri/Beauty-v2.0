package com.devrock.beautyappv2.auth

import android.util.Log
import androidx.lifecycle.ViewModel

class AuthViewModel(): ViewModel() {

    fun setButtonAvailability(count: Int) : Boolean {
        return count >= 18
    }

}