package com.devrock.beautyappv2.rent.hourly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentHourlyTimeslotsBinding
import com.devrock.beautyappv2.net.TimeSlot
import com.devrock.beautyappv2.rent.ClosePromptFragment
import com.devrock.beautyappv2.util.getFormattedMonth
import kotlinx.android.synthetic.main.timeslot_grid_item.view.*
import kotlin.properties.Delegates

class HourlyTimeslotsFragment : Fragment() {

    private lateinit var binding: FragmentHourlyTimeslotsBinding

    private lateinit var morningAdapter: TimeslotsAdapter
    private lateinit var dayAdapter: TimeslotsAdapter
    private lateinit var eveningAdapter: TimeslotsAdapter
    private var hourPrice = 0
    private var resultPrice = 0


    private val viewModel: HourlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HourlyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHourlyTimeslotsBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val date = HourlyTimeslotsFragmentArgs.fromBundle(arguments!!).selectedDate

        val selectedMonth = getFormattedMonth(date.substring(5, 7))
        val formattedDate = "${date.substring(8, 10)} $selectedMonth ${date.substring(0, 4)}"

        binding.selectedDate.text = formattedDate

        val prefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        val salonId = prefs.getInt("salonId", 0)

        viewModel.getTimeslots(salonId, date)
        viewModel.getHourPrices(1)

        binding.popup.setOnClickListener { v ->
            v.visibility = View.GONE
        }

        binding.buttonBack.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }

        val closePrompt = ClosePromptFragment().newInstance()

        binding.buttonClose.setOnClickListener {

            closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

        }

        viewModel.timeslotsRaw.observe(this, Observer {

            if(it != null) {
                viewModel.sortTimeslots(it)
                morningAdapter = TimeslotsAdapter(context!!, viewModel.timeslotsMorning)
                dayAdapter = TimeslotsAdapter(context!!, viewModel.timeslotsDay)
                eveningAdapter = TimeslotsAdapter(context!!, viewModel.timeslotsEvening)
                binding.morningTimeslotsGrid.adapter = morningAdapter
                binding.dayTimeslotsGrid.adapter = dayAdapter
                binding.evnTimeslotsGrid.adapter = eveningAdapter

                viewModel.selectedTimeslots.value!!.clear()


                binding.morningTimeslotsGrid.setOnItemClickListener( object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        if (!viewModel.selectedTimeslots.value!!.contains(binding.morningTimeslotsGrid.adapter.getItem(position))) {
                            viewModel.addTimeslot(binding.morningTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_checked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral5))

                        } else {
                            viewModel.removeTimeslot(binding.morningTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_unchecked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral6))

                        }
                    }

                })

                binding.dayTimeslotsGrid.setOnItemClickListener( object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (!viewModel.selectedTimeslots.value!!.contains(binding.dayTimeslotsGrid.adapter.getItem(position))) {
                            viewModel.addTimeslot(binding.dayTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_checked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral5))

                        } else {
                            viewModel.removeTimeslot(binding.dayTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_unchecked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral6))

                        }
                    }

                })

                binding.evnTimeslotsGrid.setOnItemClickListener( object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (!viewModel.selectedTimeslots.value!!.contains(binding.evnTimeslotsGrid.adapter.getItem(position))) {
                            viewModel.addTimeslot(binding.evnTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_checked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral5))
                        } else {
                            viewModel.removeTimeslot(binding.evnTimeslotsGrid.adapter.getItem(position) as TimeSlot)
                            view!!.setBackgroundResource(R.drawable.timeslot_unchecked)
                            view.timeslotTime.setTextColor(resources.getColor(R.color.neutral6))
                        }
                    }

                })

            }

        })

        viewModel.selectedTimeslots.observe(this, Observer {

            viewModel.hourPrices.observe(this, Observer { list ->

                if (list != null) {

                    for (price in viewModel.hourPrices.value!!) {

                        if (it.size >= price.hours) {
                            binding.hourPrice.text = "${price.price / price.hours} ₽ / час"
                            hourPrice = price.price / price.hours
                        }
                    }

                    popupLoop@ for (price in viewModel.hourPrices.value!!) {
                        if (it.isNotEmpty() && it.size == price.hours - 1) {
                            binding.popupPrice.text = "от ${price.hours} часов - ${price.price / price.hours} ₽ / час"
                            binding.popup.visibility = View.VISIBLE
                            break@popupLoop
                        } else binding.popup.visibility = View.GONE
                    }

                    resultPrice = it.size * hourPrice

                    prefEditor
                        .putInt("resultPrice", resultPrice)
                        .putInt("hourPrice", hourPrice)
                        .apply()

                    binding.resultPrice.text = "Итого: ${it.size * hourPrice} ₽"
                }

            })




        })

        binding.nextButton.setOnClickListener {
            it.findNavController().navigate(HourlyTimeslotsFragmentDirections.actionHourlyTimeslotsFragmentToHourlyConfirmFragment(date))
        }



        return binding.root
    }



}

class TimeslotsAdapter(var context: Context, list: MutableList<TimeSlot?>) : BaseAdapter() {

    lateinit var inflater: LayoutInflater
    var data: MutableList<TimeSlot?> = list

    lateinit var view: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = this.data[position]

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.timeslot_grid_item, null)

        view.timeslotTime.text = "${item!!.start!!.removeRange(5, item.start!!.length)}-${item.end!!.removeRange(5, item.end!!.length)}"

        return view
    }

    override fun getItem(position: Int): TimeSlot? {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}