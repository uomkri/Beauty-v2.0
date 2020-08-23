package com.devrock.beautyappv2

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.amplitude.api.Amplitude
import com.devrock.beautyappv2.map.MapFragment
import com.devrock.beautyappv2.net.BeautyFirebaseMessagingService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class AppActivity : AppCompatActivity() {

    fun getActiveFragment() : Fragment? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return null
        }
        val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        setUpNavigation()

        val prefs: SharedPreferences = this.getSharedPreferences("StatusBarHeight", Context.MODE_PRIVATE)





        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR



            val messagingService = BeautyFirebaseMessagingService()

            val amplitude = Amplitude.getInstance()

            amplitude.initialize(context, getString(R.string.amplitude_apikey))
            amplitude.enableForegroundTracking(application)
            amplitude.enableCoppaControl()

            val firebaseInstance = FirebaseInstanceId.getInstance()

            firebaseInstance.instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("Firebase", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result?.token

                    val msg = "Token: ${token.toString()}"
                    // TODO: Remove Firebase token logging to Amplitude (on public alpha)
                    //amplitude.logEvent("Token: $token")


                    Log.d("Firebase", msg)
                })
        }
    }



    private fun setUpNavigation(){
        val navBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val nhf: NavHostFragment = supportFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(navBar, nhf.navController)
    }
}