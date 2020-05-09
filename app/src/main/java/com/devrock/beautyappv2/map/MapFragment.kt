package com.devrock.beautyappv2.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentMapBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MapFragment : Fragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    private val MAP_APIKEY = "1b5f933a-6fcb-4d7f-86b3-3ff7b972bdaf"

    private val TARGET : Point = Point(56.838000, 60.601513)

    private lateinit var mapview: MapView

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        MapKitFactory.setApiKey(MAP_APIKEY)
        MapKitFactory.initialize(context)
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMapBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val session = activity?.intent?.getStringExtra("session")



        /* val navBar = binding.bottomNavBar


         navBar.setOnNavigationItemReselectedListener {
             when (it.itemId) {
                 R.id.action_account -> {
                     navBar.findNavController().navigate(MapFragmentDirections.actionMapFragmentToProfileFragment(session))
                 }
                 R.id.action_workplaces -> {
                     navBar.findNavController().navigate(MapFragmentDirections.actionMapFragmentToWorkplacesFragment(session))
                 }
             }
         }*/

        mapview = binding.mapView
        mapview.map.move(
            CameraPosition(TARGET, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 1f),
            null
        )

        return binding.root
    }

    override fun onStop() {
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapview.onStart()
    }

}