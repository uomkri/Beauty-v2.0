package com.devrock.beautyappv2.salon

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentSalonBinding
import com.devrock.beautyappv2.salon.pages.SalonFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.rent_price_item.view.*

class SalonFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
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

        val pager = binding.salonViewpager

        val PHOTOS_ENDPOINT = "https://beauty.judoekb.ru/api/salons/photo"


        pager.adapter = SalonFragmentStateAdapter(activity!!)

        TabLayoutMediator(binding.salonTabs, binding.salonViewpager) {tab, position ->
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

                binding.salonPageRating.text = "${"%.1f".format(it.info.rating.toFloat())}"
                Glide.with(this).load(stock)
                    .into(binding.salonThumbnail)
            }
        })

        return binding.root
    }


}