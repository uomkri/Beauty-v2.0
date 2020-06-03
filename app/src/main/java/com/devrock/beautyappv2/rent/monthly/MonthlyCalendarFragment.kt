package com.devrock.beautyappv2.rent.monthly

import android.content.Context
import android.content.SharedPreferences
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
import com.devrock.beautyappv2.databinding.FragmentMonthlyCalendarBinding
import com.devrock.beautyappv2.rent.ClosePromptFragment
import java.util.*

class MonthlyCalendarFragment : Fragment() {
    private lateinit var binding: FragmentMonthlyCalendarBinding

    private val viewModel: MonthlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MonthlyViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMonthlyCalendarBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        binding.calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

                val prefs: SharedPreferences = activity!!.getSharedPreferences("EntryInfo", Context.MODE_PRIVATE)
                val prefEditor: SharedPreferences.Editor = prefs.edit()

                val startAfter = prefs.getString("startAfter", null)

                val startCalendar = Calendar.getInstance()
                startCalendar.set(startAfter!!.substring(0, 4).toInt(), startAfter.substring(5, 7).toInt(), startAfter.substring(8, 10).toInt())
                Log.i("CDM", "${startAfter!!.substring(0, 4)}-${startAfter.substring(5, 7)}-${startAfter.substring(8, 10)}")

                binding.calendarView.setMinimumDate(startCalendar)

                val calendar = eventDay.calendar
                val month = calendar.get(GregorianCalendar.MONTH)
                val year = calendar.get(GregorianCalendar.YEAR)
                val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)

                var monthFormat = ""
                var dayFormat = ""

                monthFormat = if (month < 10) {
                    "0${month + 1}"
                } else {
                    "${month + 1}"
                }

                dayFormat = if (day < 10) {
                    "0${day}"
                } else {
                    "${day}"
                }

                viewModel.selectedDate.value = "$year-${monthFormat}-$dayFormat"

                prefEditor.putString("selectedDate", viewModel.selectedDate.value)

                Log.i("CD", viewModel.selectedDate.value)

                val closePrompt = ClosePromptFragment().newInstance()

                binding.buttonClose.setOnClickListener {

                    closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

                }

            }

        })

        binding.nextButton.setOnClickListener {
            it.findNavController().navigate(MonthlyCalendarFragmentDirections.actionMonthlyCalendarFragmentToMonthlyConfirmFragment())
        }

        return binding.root
    }
}