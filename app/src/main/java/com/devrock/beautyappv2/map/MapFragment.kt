package com.devrock.beautyappv2.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentMapBinding
import com.devrock.beautyappv2.net.SalonListItem
import com.devrock.beautyappv2.signup.userpic.PERMISSION_REQUEST_CODE
import com.devrock.beautyappv2.util.BeautyUtils
import com.devrock.beautyappv2.util.getBitmapFromVectorDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.*
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.android.synthetic.main.map_popup.*
import kotlinx.android.synthetic.main.map_popup.view.*
import kotlinx.android.synthetic.main.map_popup.view.calendar
import kotlinx.android.synthetic.main.map_popup.view.clock
import kotlinx.android.synthetic.main.map_popup.view.map_popup
import kotlinx.android.synthetic.main.map_popup.view.salonAddress
import kotlinx.android.synthetic.main.map_popup.view.salonDistance
import kotlinx.android.synthetic.main.map_popup.view.salonName
import kotlinx.android.synthetic.main.map_popup.view.star1
import kotlinx.android.synthetic.main.map_popup.view.star2
import kotlinx.android.synthetic.main.map_popup.view.star3
import kotlinx.android.synthetic.main.map_popup.view.star4
import kotlinx.android.synthetic.main.map_popup.view.star5
import kotlinx.android.synthetic.main.map_popup.view.startDay
import kotlinx.android.synthetic.main.map_popup.view.workingHours
import kotlinx.android.synthetic.main.modal_test.*
import kotlinx.android.synthetic.main.modal_test.view.*
import kotlin.properties.Delegates

class MapFragment : Fragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    private val MAP_APIKEY = "1b5f933a-6fcb-4d7f-86b3-3ff7b972bdaf"

    private val TARGET: Point = Point(56.838000, 60.601513)

    private lateinit var mapview: MapView

    private lateinit var binding: FragmentMapBinding

    private var userLon by Delegates.notNull<Double>()

    private var userLat by Delegates.notNull<Double>()

    private lateinit var mapObjectCollection: MapObjectCollection


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val limit = 15
        val offset = 0
        val order = "distance"

        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }


        MapKitFactory.setApiKey(MAP_APIKEY)
        MapKitFactory.initialize(context)
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMapBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val session = activity?.intent?.getStringExtra("session")



        mapview = binding.mapView

        val mapKit = MapKitFactory.getInstance()



        fun openPopup(item: SalonListItem) {

            mapview.map.move(
                CameraPosition(Point(item.geo.latitude, item.geo.longitude), 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )

            var bottomSheetBehavior = BottomSheetBehavior.from(modal_test)

            mapview.map.addInputListener(object : InputListener {
                override fun onMapLongTap(p0: Map, p1: Point) {

                }

                override fun onMapTap(p0: Map, p1: Point) {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) bottomSheetBehavior.state =
                        BottomSheetBehavior.STATE_COLLAPSED
                }

            })

            val view = activity!!.findViewById<View>(R.id.modal_test)

            var salonName = view.salonName
            val salonAddress = view.salonAddress
            val salonDistance = view.salonDistance
            val workingHours = view.workingHours
            val startDay = view.startDay
            val stars = listOf(view.star1, view.star2, view.star3, view.star4, view.star5)

            salonName.text = item.name
            salonAddress.text = item.geo.address
            salonDistance.text = "${"%.1f".format(item.distance?.div(1000))} км"

            if (item.hourRentStart != null) {
                workingHours.text = "почасовая аренда"
                workingHours.visibility = View.VISIBLE
                view.clock.visibility = View.VISIBLE
            } else {
                workingHours.visibility = View.INVISIBLE
                view.clock.visibility = View.INVISIBLE
            }

            if (item.daysRentStart != null) {
                startDay.text = "помесячная аренда"
                startDay.visibility = View.VISIBLE
                view.calendar.visibility = View.VISIBLE
            } else {
                startDay.visibility = View.INVISIBLE
                view.calendar.visibility = View.INVISIBLE
            }

            val r = item.rating

            when {
                (r > 1.0) -> {
                    stars[0].setImageResource(R.drawable.ic_star_inactive)
                    stars[1].setImageResource(R.drawable.ic_star_inactive)
                    stars[2].setImageResource(R.drawable.ic_star_inactive)
                    stars[3].setImageResource(R.drawable.ic_star_inactive)
                    stars[4].setImageResource(R.drawable.ic_star_inactive)
                }
                (r > 1.0 && r < 1.5) || r == 1 -> {
                    stars[0].setImageResource(R.drawable.ic_star_active)
                    stars[1].setImageResource(R.drawable.ic_star_inactive)
                    stars[2].setImageResource(R.drawable.ic_star_inactive)
                    stars[3].setImageResource(R.drawable.ic_star_inactive)
                    stars[4].setImageResource(R.drawable.ic_star_inactive)
                }
                (r > 1.5 && r < 2.5) || r == 2 -> {
                    stars[0].setImageResource(R.drawable.ic_star_active)
                    stars[1].setImageResource(R.drawable.ic_star_active)
                    stars[2].setImageResource(R.drawable.ic_star_inactive)
                    stars[3].setImageResource(R.drawable.ic_star_inactive)
                    stars[4].setImageResource(R.drawable.ic_star_inactive)
                }
                (r > 2.5 && r < 3.5) || r == 3 -> {
                    stars[0].setImageResource(R.drawable.ic_star_active)
                    stars[1].setImageResource(R.drawable.ic_star_active)
                    stars[2].setImageResource(R.drawable.ic_star_active)
                    stars[3].setImageResource(R.drawable.ic_star_inactive)
                    stars[4].setImageResource(R.drawable.ic_star_inactive)
                }
                (r > 3.5 && r < 4.5) || r == 4 -> {
                    stars[0].setImageResource(R.drawable.ic_star_active)
                    stars[1].setImageResource(R.drawable.ic_star_active)
                    stars[2].setImageResource(R.drawable.ic_star_active)
                    stars[3].setImageResource(R.drawable.ic_star_active)
                    stars[4].setImageResource(R.drawable.ic_star_inactive)
                }
                r >= 5 -> {
                    stars[0].setImageResource(R.drawable.ic_star_active)
                    stars[1].setImageResource(R.drawable.ic_star_active)
                    stars[2].setImageResource(R.drawable.ic_star_active)
                    stars[3].setImageResource(R.drawable.ic_star_active)
                    stars[4].setImageResource(R.drawable.ic_star_active)
                }
            }

            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

        }

        mapKit.createLocationManager().requestSingleUpdate(object : LocationListener {
            override fun onLocationStatusUpdated(p0: LocationStatus) {
            }

            override fun onLocationUpdated(p0: Location) {
                mapview.map.move(
                    CameraPosition(p0.position, 14.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )
            }

        })

        mapKit.createLocationManager().subscribeForLocationUpdates(
            3.0,
            10000,
            0.0,
            false,
            FilteringMode.OFF,
            object : LocationListener {
                override fun onLocationStatusUpdated(p0: LocationStatus) {
                }

                override fun onLocationUpdated(p0: Location) {
                    Log.d("Location", "${p0.position.latitude} ${p0.position.longitude}")

                    userLat = p0.position.latitude
                    userLon = p0.position.longitude

                    viewModel.getSalonsList(userLon, userLat, limit, offset, order, session!!)

                }

            })

        viewModel.status.observe(this, Observer {
            if (it == "Ok") {
                Log.i("LS", viewModel.salonList.value.toString())
                viewModel.salonList.observe(this, Observer { list ->
                    if (list != null) {
                        for (item: SalonListItem in viewModel.salonList.value!!) {
                            val point = Point(item.geo.latitude, item.geo.longitude)

                            val bm = getBitmapFromVectorDrawable(context!!, R.drawable.ic_marker)

                            val tapArea = Rect(PointF(0.0f, 0.0f), PointF(20.0f, 20.0f))

                            mapview.map.mapObjects.addPlacemark(
                                point,
                                ImageProvider.fromBitmap(bm),
                                IconStyle().setAnchor(PointF(0.5f, 1.0f)).setScale(0.7f)
                            )
                                .addTapListener { mapObject, point ->
                                    openPopup(item)
                                    true
                                }
                        }
                    }
                })

            }
        })

        val userLocationLayer = mapKit.createUserLocationLayer(mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true




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

