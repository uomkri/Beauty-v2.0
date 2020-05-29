package com.devrock.beautyappv2.rent.hourly

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.devrock.beautyappv2.databinding.FragmentHourlyCalendarBinding
import java.util.*

class HourlyCalendarFragment : Fragment() {

    private lateinit var binding: FragmentHourlyCalendarBinding

    private val viewModel: HourlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HourlyViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHourlyCalendarBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        binding.calendarView.setOnDayClickListener( object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val calendar = eventDay.calendar
                val month = calendar.get(GregorianCalendar.MONTH)
                val year = calendar.get(GregorianCalendar.YEAR)
                val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)

                if (month < 10) {
                    viewModel.selectedDate.value = "$year-0${month+1}-$day"
                }  else {
                    viewModel.selectedDate.value = "$year-${month+1}-$day"
                }
                Log.i("CD", viewModel.selectedDate.value)

            }

        })

        binding.nextButton.setOnClickListener {
            it.findNavController().navigate(HourlyCalendarFragmentDirections.actionHourlyCalendarFragmentToHourlyTimeslotsFragment(viewModel.selectedDate.value!!))
        }

        return binding.root
    }

}