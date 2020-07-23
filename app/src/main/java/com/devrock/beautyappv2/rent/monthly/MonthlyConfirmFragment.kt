package com.devrock.beautyappv2.rent.monthly

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
import com.devrock.beautyappv2.databinding.FragmentMonthlyConfirmBinding
import com.devrock.beautyappv2.net.TimeSlot
import com.devrock.beautyappv2.rent.ClosePromptFragment
import com.devrock.beautyappv2.util.getFormattedMonth
import kotlinx.android.synthetic.main.confirm_list_item.view.*

class MonthlyConfirmFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyConfirmBinding

    private val viewModel: MonthlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MonthlyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMonthlyConfirmBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val date = viewModel.selectedDate.value

        val prefs: SharedPreferences = activity!!.getSharedPreferences("SalonInfo", Context.MODE_PRIVATE)
        val offerPrefs: SharedPreferences = activity!!.getSharedPreferences("EntryInfo", Context.MODE_PRIVATE)
        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)


        val salonName = prefs.getString("name", null)
        val salonAddress = prefs.getString("address", null)
        val salonRating = prefs.getString("rating", null)
        val resultPrice = offerPrefs.getInt("resultPrice", 0)
        val session = sessionPrefs.getString("session", null)
        val rentType = offerPrefs.getString("rentType", null)
        val offerId = offerPrefs.getInt("offerId", 0)

        val selectedMonth = getFormattedMonth(date!!.substring(5, 7))
        val formattedDate = "${date.substring(8, 10)} $selectedMonth ${date.substring(0, 4)}"

        binding.salonName.text = salonName
        binding.salonAddress.text = salonAddress
        binding.salonRating.text = salonRating
        binding.startDay.text = formattedDate
        binding.resultPrice.text = "Итого: $resultPrice ₽"
        binding.rentType.text = rentType

        binding.buttonBack.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        val closePrompt = ClosePromptFragment().newInstance()

        binding.buttonClose.setOnClickListener {

            closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

        }

        binding.buttonEditType.setOnClickListener {
            it.findNavController().navigate(MonthlyConfirmFragmentDirections.actionMonthlyConfirmFragmentToMonthlyOffersFragment())
        }

        binding.buttonEditStart.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()

        }

        binding.doneButton.setOnClickListener {
            viewModel.addMonthEntry(session = session!!, offerId = offerId, startDate = date!!)

            viewModel.status.observe(this, Observer { s ->

                if (s == "Ok") {
                    it.findNavController().navigate(MonthlyConfirmFragmentDirections.actionMonthlyConfirmFragmentToRentSuccessFragment())
                }

            })

        }

        val endMonth = date!!.substring(5, 7).toInt() + 1

        var endMonthString = ""

        endMonthString = if (endMonth < 10) "0$endMonth" else "$endMonth"

        val endDate = "${date!!.substring(0, 4)}-$endMonthString-${date!!.substring(8, 10)}"
        binding.endDay.text = endDate



        /*viewModel.addStatus.observe(this, Observer {

            if (it == "Ok") {
                binding.root.findNavController().navigate(HourlyConfirmFragmentDirections.actionHourlyConfirmFragmentToRentSuccessFragment())
            }

        })*/



        return binding.root

    }


}