package com.devrock.beautyappv2.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentMapBinding
import com.devrock.beautyappv2.net.SalonListItem
import com.devrock.beautyappv2.signup.userpic.PERMISSION_REQUEST_CODE
import com.devrock.beautyappv2.util.getBitmapFromVectorDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.map_popup.view.calendar
import kotlinx.android.synthetic.main.map_popup.view.clock
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
import kotlinx.android.synthetic.main.modal_test2.*
import kotlin.properties.Delegates

class MapFragment : Fragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    private lateinit var mapView: com.google.android.gms.maps.MapView

    private lateinit var map: GoogleMap

    private lateinit var binding: FragmentMapBinding

    private lateinit var markers: List<Marker>

    private var userLon by Delegates.notNull<Double>()

    private var userLat by Delegates.notNull<Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val limit = 15
        val offset = 0
        val order = "distance"

        //request permissions
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

        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMapBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val session = activity?.intent?.getStringExtra("session")

        mapView = binding.mapView

        mapView.onCreate(savedInstanceState)

        mapView.onResume()

        try {
            MapsInitializer.initialize(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync {GoogleMap ->
            map = GoogleMap

            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false

            getDeviceLocation(limit, offset, order, session!!)


            //check if api call was successful and place markers
            viewModel.status.observe(this, Observer {
                if (it == "Ok") {
                    viewModel.salonList.observe(this, Observer { list ->
                        if (list != null) {
                            for (item: SalonListItem in viewModel.salonList.value!!) {
                                val point = LatLng(item.geo.latitude, item.geo.longitude)

                                val bm = getBitmapFromVectorDrawable(context!!, R.drawable.ic_marker)

                                val marker = map.addMarker( MarkerOptions().position(point).title(item.name).icon(BitmapDescriptorFactory.fromBitmap(bm)))
                                marker.tag = item
                            }
                        }
                    })

                }
            })

            map.setOnMarkerClickListener {

                val markerInfo = it.tag as SalonListItem
                openPopup(markerInfo, session)
                map.moveCamera(CameraUpdateFactory.zoomTo(17.0f))
                false
            }


        }








        return binding.root
    }

    fun openPopup(item: SalonListItem, session: String) {


        var bottomSheetBehavior = BottomSheetBehavior.from(modal_test2)

        map.setOnMapClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = activity!!.findViewById<View>(R.id.modal_test2)

        val popup = binding.root.findViewById<MaterialCardView>(R.id.popup_card)


        popup.setOnClickListener {
            popup.findNavController().navigate(MapFragmentDirections.actionMapFragmentToSalonFragment(item.id, session, item.geo.longitude, item.geo.latitude))
        }

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
        } else {
            workingHours.visibility = View.INVISIBLE
        }

        if (item.daysRentStart != null) {
            startDay.text = "помесячная аренда"
            startDay.visibility = View.VISIBLE
        } else {
            startDay.visibility = View.INVISIBLE
        }

        val r = item.rating
        when {
            (r.toDouble() < 0.5) -> {
                stars[0].setImageResource(R.drawable.ic_star_inactive)
                stars[1].setImageResource(R.drawable.ic_star_inactive)
                stars[2].setImageResource(R.drawable.ic_star_inactive)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            (r.toDouble() >= 0.5 && r < 1.0) -> {
                stars[0].setImageResource(R.drawable.ic_star_half)
                stars[1].setImageResource(R.drawable.ic_star_inactive)
                stars[2].setImageResource(R.drawable.ic_star_inactive)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            (r.toDouble() >= 1.0 && r < 1.5) -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_inactive)
                stars[2].setImageResource(R.drawable.ic_star_inactive)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            (r.toDouble() >= 1.5 && r < 2.0) -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_half)
                stars[2].setImageResource(R.drawable.ic_star_inactive)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            (r.toDouble() >= 2.0 && r < 2.5) -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_inactive)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            r.toDouble() >= 2.5 && r < 3.0 -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_half)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            r.toDouble() >= 3.0 && r < 3.5 -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_active)
                stars[3].setImageResource(R.drawable.ic_star_inactive)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            r.toDouble() >= 3.5 && r < 4.0 -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_active)
                stars[3].setImageResource(R.drawable.ic_star_half)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            r.toDouble() >= 4.0 && r < 4.5 -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_active)
                stars[3].setImageResource(R.drawable.ic_star_active)
                stars[4].setImageResource(R.drawable.ic_star_inactive)
            }
            r.toDouble() >= 4.5 && r < 5.0 -> {
                stars[0].setImageResource(R.drawable.ic_star_active)
                stars[1].setImageResource(R.drawable.ic_star_active)
                stars[2].setImageResource(R.drawable.ic_star_active)
                stars[3].setImageResource(R.drawable.ic_star_active)
                stars[4].setImageResource(R.drawable.ic_star_half)
            }
            r.toDouble() >= 5.0-> {
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

    fun getDeviceLocation(limit: Int, offset: Int, order: String, session: String) {
        try {

            val mFusedLocationProviderClient = FusedLocationProviderClient(context!!)

            val locationResult: Task<Location>? = mFusedLocationProviderClient.lastLocation
            mFusedLocationProviderClient.lastLocation?.addOnCompleteListener(activity!!
            ) { p0 ->
                if (p0.isSuccessful) {
                    val mLastKnownLocation = p0.result
                    userLon = mLastKnownLocation!!.longitude
                    userLat = mLastKnownLocation!!.latitude
                    viewModel.getSalonsList(userLon, userLat, limit, offset, order, session)

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLat, userLon), 14f))
                }
            }
        } catch(e: SecurityException)  {
            Log.e("Exception: %s", e.message);
        }
    }

    fun updateLocationUI() {
        if (map != null) {
            return
        }
        try {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            Log.e("MAP", e.message)
        }
    }





    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        val mgr = MapStateManager(context!!)
        mgr.saveMapState(map)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    fun setupMapIfNeeded() {

    }

    }

