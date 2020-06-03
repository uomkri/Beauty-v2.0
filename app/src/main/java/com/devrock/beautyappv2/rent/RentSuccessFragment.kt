package com.devrock.beautyappv2.rent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.devrock.beautyappv2.databinding.FragmentRentSuccessBinding

class RentSuccessFragment : Fragment() {

    private lateinit var binding: FragmentRentSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRentSuccessBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val session = sessionPrefs.getString("session", null)


        binding.buttonLater.setOnClickListener {

            it.findNavController().navigate(RentSuccessFragmentDirections.actionRentSuccessFragmentToMapFragment())

        }

        return binding.root
    }

}