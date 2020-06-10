package com.devrock.beautyappv2.rent.hourly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentHourlyConfirmBinding
import com.devrock.beautyappv2.net.TimeSlot
import com.devrock.beautyappv2.rent.ClosePromptFragment
import com.devrock.beautyappv2.util.getFormattedMonth
import kotlinx.android.synthetic.main.confirm_list_item.view.*

class HourlyConfirmFragment : Fragment() {

    private lateinit var binding: FragmentHourlyConfirmBinding

    private val viewModel: HourlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HourlyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHourlyConfirmBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val date = HourlyConfirmFragmentArgs.fromBundle(arguments!!).selectedDate

        val prefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)


        val salonName = prefs.getString("name", null)
        val salonAddress = prefs.getString("address", null)
        val salonRating = prefs.getString("rating", null)
        val resultPrice = prefs.getInt("resultPrice", 0)
        val hourPrice = prefs.getInt("hourPrice", 0)
        val session = sessionPrefs.getString("session", null)

        val selectedMonth = getFormattedMonth(date!!.substring(5, 7))
        val formattedDate = "${date.substring(8, 10)} $selectedMonth ${date.substring(0, 4)}"

        binding.salonName.text = salonName
        binding.salonAddress.text = salonAddress
        binding.salonRating.text = salonRating
        binding.selectedDate.text = formattedDate
        binding.hourPrice.text = "$hourPrice ₽ / час"
        binding.resultPrice.text = "Итого: $resultPrice ₽"

        val closePrompt = ClosePromptFragment().newInstance()

        binding.buttonClose.setOnClickListener {

            closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

        }

        binding.buttonBack.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        binding.buttonEdit.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        binding.doneButton.setOnClickListener {

            viewModel.addHourEntry(session = session!!, price = resultPrice, slots = viewModel.selectedTimeslots.value!!.toList() as List<TimeSlot>)

        }

        viewModel.addStatus.observe(this, Observer {

            if (it == "Ok") {
                binding.root.findNavController().navigate(HourlyConfirmFragmentDirections.actionHourlyConfirmFragmentToRentSuccessFragment())
            }

        })

        viewModel.selectedTimeslots.observe(this, Observer {

            if (it != null && it.size > 0) {

                val slots = it

                val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


                for (slot in slots) {

                    val view = inflater.inflate(R.layout.confirm_list_item, null)

                    val insertPoint = binding.confirmBody

                    if (slot!!.start!!.length > 1 && slot!!.end!!.length > 1) view.itemSelectedTime.text = "${slot!!.start!!.substring(0, 5)}-${slot.end!!.substring(0, 5)}"

                    insertPoint.addView(view)

                }

            }

        })

        return binding.root

    }

}