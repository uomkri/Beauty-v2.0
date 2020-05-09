package com.devrock.beautyappv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        setUpNavigation()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun setUpNavigation(){
        val navBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val nhf: NavHostFragment = supportFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(navBar, nhf.navController)
    }
}