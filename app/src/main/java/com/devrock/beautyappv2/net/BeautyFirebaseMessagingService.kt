package com.devrock.beautyappv2.net

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class BeautyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        sendRegistrationToServer(p0)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("Firebase", "send token: $token")
        // TODO: Implement Send Firebase Registration To Api
    }

}