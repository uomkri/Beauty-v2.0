package com.devrock.beautyappv2.rent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.databinding.FragmentRentSuccessBinding
import com.devrock.beautyappv2.salon.SalonViewModel

class RentSuccessFragment : Fragment() {

    private lateinit var binding: FragmentRentSuccessBinding

    private val viewModel: RentSuccessViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RentSuccessViewModel::class.java)
    }

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

        viewModel.getLatestEntry(session!!)

        viewModel.latestEntry.observe(this, Observer {
            if (it != null) {
                binding.buttonShowEntry.setOnClickListener { v ->

                    v.findNavController().navigate(RentSuccessFragmentDirections.actionRentSuccessFragmentToEntryInfoFragment(it.id))

                }
            }
        })


        binding.buttonLater.setOnClickListener {

            it.findNavController().navigate(RentSuccessFragmentDirections.actionRentSuccessFragmentToMapFragment())

        }



        return binding.root
    }

}