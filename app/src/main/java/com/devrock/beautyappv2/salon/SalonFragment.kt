package com.devrock.beautyappv2.salon

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.databinding.FragmentSalonBinding
import com.devrock.beautyappv2.salon.pages.SalonViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SalonFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(this).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentSalonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSalonBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        val session = SalonFragmentArgs.fromBundle(arguments!!).session
        val salonId = SalonFragmentArgs.fromBundle(arguments!!).id
        val longitude = SalonFragmentArgs.fromBundle(arguments!!).longitude
        val latitude = SalonFragmentArgs.fromBundle(arguments!!).latitude

        viewModel.getSalonById(salonId, longitude, latitude)

        binding.salonViewpager.adapter = SalonViewPagerAdapter()

        TabLayoutMediator(binding.salonTabs, binding.salonViewpager) {tab, position ->
            when (position) {
                0 -> tab.text = "Информация"
                1 -> tab.text = "Цены"
                2 -> tab.text = "Фото"
                3 -> tab.text = "Отзывы"
            }
        }.attach()

        viewModel.salonInfo.observe(this, Observer {
            if (it != null) {
                binding.salonPageName.text = it.info.name
                binding.salonPageRating.text = "${"%.1f".format(it.info.rating.toFloat())}"
            }
        })

        return binding.root
    }


}