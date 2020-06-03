package com.devrock.beautyappv2.salon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.databinding.FragmentRentVariantsBinding

class RentVariantsFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentRentVariantsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRentVariantsBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val prefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val sessionPrfs = activity!!.getSharedPreferences("Session", 0)

        val salonId = prefs.getInt("salonId", 0)
        val longitude = prefs.getFloat("longitude", 0f).toDouble()
        val latitude = prefs.getFloat("latitude", 0f).toDouble()
        val session = sessionPrfs.getString("session", null)

        binding.buttonClose.setOnClickListener {
            it.findNavController().navigate(RentVariantsFragmentDirections.actionRentVariantsFragmentToSalonFragment(salonId, session!!, longitude, latitude))
        }

        binding.buttonHourly.setOnClickListener {
            it.findNavController().navigate(RentVariantsFragmentDirections.actionRentVariantsFragmentToHourlyCalendarFragment())
        }

        binding.buttonMonthly.setOnClickListener {
           it.findNavController().navigate(RentVariantsFragmentDirections.actionRentVariantsFragmentToMonthlyOffersFragment())
        }

        return binding.root
    }

}