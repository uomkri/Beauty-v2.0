package com.devrock.beautyappv2.salon.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentPricesBinding
import com.devrock.beautyappv2.salon.SalonViewModel
import com.github.underscore.U.indexOf
import kotlinx.android.synthetic.main.prices_item.view.*

class PricesFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentPricesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPricesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewModel.salonInfo.observe(this, Observer {

            if (it != null) {

                val prices = viewModel.salonInfo.value!!.hourPrices

                for (item in prices) {
                    val view = inflater.inflate(R.layout.prices_item, null)

                    view.hours.text = "от ${item.hours} ч."
                    view.price.text = "${item.price} ₽ / час"

                    binding.hourlyInsertPoint.addView(view)

                    Log.e("hrs", item.hours.toString())
                    Log.e("prc", item.price.toString())
                }

            }

        })

        return binding.root
    }

}