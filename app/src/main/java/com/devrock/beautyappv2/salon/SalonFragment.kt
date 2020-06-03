package com.devrock.beautyappv2.salon

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentSalonBinding
import com.devrock.beautyappv2.salon.pages.SalonFragmentStateAdapter
import com.devrock.beautyappv2.util.getBitmapFromVectorDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.rent_price_item.view.*

class SalonFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentSalonBinding

    private lateinit var mapView: com.google.android.gms.maps.MapView

    private var map: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSalonBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        mapView = binding.googleMap

        mapView.onCreate(savedInstanceState)

        mapView.onResume()

        activity!!.bottomNavBar.visibility = View.GONE

        val session = SalonFragmentArgs.fromBundle(arguments!!).session
        val salonId = SalonFragmentArgs.fromBundle(arguments!!).id
        val longitude = SalonFragmentArgs.fromBundle(arguments!!).longitude
        val latitude = SalonFragmentArgs.fromBundle(arguments!!).latitude

        var rentType: String? = null

        viewModel.session.value = session

        viewModel.getSalonById(salonId, longitude, latitude)

        val prefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        val pager = binding.salonViewpager

        val PHOTOS_ENDPOINT = "https://beauty.judoekb.ru/api/salons/photo"

        binding.backButton.setOnClickListener {
            prefEditor
                .clear()
                .apply()
            it.findNavController().navigate(SalonFragmentDirections.actionSalonFragmentToMapFragment())
        }

        binding.rentButton.setOnClickListener {

            if (rentType != null) {

                when (rentType) {
                    "Both" -> it.findNavController().navigate(SalonFragmentDirections.actionSalonFragmentToRentVariantsFragment(salonId))
                    "Hourly" -> it.findNavController().navigate(SalonFragmentDirections.actionSalonFragmentToHourlyCalendarFragment())
                    "Monthly" -> it.findNavController().navigate(SalonFragmentDirections.actionSalonFragmentToMonthlyOffersFragment())
                }

            }

        }

        pager.adapter = SalonFragmentStateAdapter(activity!!)

        TabLayoutMediator(binding.salonTabs, binding.salonViewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Инфо"
                1 -> tab.text = "Цены"
                2 -> tab.text = "Фото"
                3 -> tab.text = "Отзывы"
            }
        }.attach()

        viewModel.salonInfo.observe(this, Observer {
            if (it != null) {
                binding.salonPageName.text = it.info.name
                val uri = "${PHOTOS_ENDPOINT}/${it.mainPhoto}"
                val stock = "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg"

                mapSetup(it.info.geo.latitude, it.info.geo.longitude)

                prefEditor
                    .putInt("salonId", it.info.id)
                    .putFloat("longitude", it.info.geo.longitude.toFloat())
                    .putFloat("latitude", it.info.geo.latitude.toFloat())
                    .putString("name", it.info.name)
                    .putString("address", it.info.geo.address)
                    .putString("rating", it.info.rating.toString())
                    .apply()

                when {
                    it.info.daysRentStart != null && it.info.hourRentStart == null -> rentType = "Monthly"
                    it.info.daysRentStart == null && it.info.hourRentStart != null -> rentType = "Hourly"
                    it.info.daysRentStart != null && it.info.hourRentStart != null -> rentType = "Both"
                }

                binding.salonPageRating.text = "${"%.1f".format(it.info.rating.toFloat())}"
            }
        })

        return binding.root
    }

    fun mapSetup(latitude: Double, longitude: Double) {

        try {
            MapsInitializer.initialize(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync {GoogleMap ->

            map = GoogleMap

            map!!.isMyLocationEnabled = true
            map!!.uiSettings.isMyLocationButtonEnabled = false

            val point = LatLng(latitude, longitude)

            val bm = getBitmapFromVectorDrawable(context!!, R.drawable.ic_marker)

            map!!.addMarker(MarkerOptions().position(point).icon(
                BitmapDescriptorFactory.fromBitmap(bm)))

            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17.0f))

            map!!.uiSettings.isScrollGesturesEnabled = false
            map!!.uiSettings.isZoomControlsEnabled = false
            map!!.uiSettings.isZoomGesturesEnabled = false

        }

    }

}