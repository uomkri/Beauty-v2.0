package com.devrock.beautyappv2.rent.hourly

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.devrock.beautyappv2.databinding.FragmentHourlyCalendarBinding
import com.devrock.beautyappv2.rent.ClosePromptFragment
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

        val closePrompt = ClosePromptFragment().newInstance()

        binding.buttonClose.setOnClickListener {

            closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

        }

        activity!!.window.apply {

            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.WHITE

        }

        val salonPrefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val sessionPrefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)

        val salonId = salonPrefs.getInt("salonId", 0)
        val lon = salonPrefs.getFloat("longitude", 0f)
        val lat = salonPrefs.getFloat("latitude", 0f)
        val session = sessionPrefs.getString("session", null)



        activity!!.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                /*binding.root.findNavController().navigate(HourlyCalendarFragmentDirections.actionHourlyCalendarFragmentToSalonFragment(
                    salonId, session!!, lon.toDouble(), lat.toDouble()
                ))*/

                closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

            }
        })

        binding.calendarView.setOnDayClickListener( object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

                val calendar = eventDay.calendar
                val month = calendar.get(GregorianCalendar.MONTH)
                val year = calendar.get(GregorianCalendar.YEAR)
                val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)

                var monthFormat = ""
                var dayFormat = ""

                monthFormat = if (month < 10) {
                    "0${month+1}"
                }  else {
                    "${month+1}"
                }

                dayFormat = if (day < 10) {
                    "0${day}"
                }  else {
                    "${day}"
                }

                viewModel.selectedDate.value = "$year-${monthFormat}-$dayFormat"

                Log.i("CD", viewModel.selectedDate.value)


            }

        })

        binding.nextButton.setOnClickListener {
            it.findNavController().navigate(HourlyCalendarFragmentDirections.actionHourlyCalendarFragmentToHourlyTimeslotsFragment(viewModel.selectedDate.value!!))
        }

        return binding.root
    }

}