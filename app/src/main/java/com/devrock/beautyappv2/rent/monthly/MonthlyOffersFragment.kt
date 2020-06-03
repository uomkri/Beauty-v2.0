package com.devrock.beautyappv2.rent.monthly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentMonthlyOffersBinding
import com.devrock.beautyappv2.net.Offer
import com.devrock.beautyappv2.net.OfferDays
import com.devrock.beautyappv2.rent.ClosePromptFragment
import kotlinx.android.synthetic.main.monthly_offer_item.view.*

class MonthlyOffersFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyOffersBinding

    private val viewModel: MonthlyViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MonthlyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMonthlyOffersBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        viewModel.getOffers()

        val prefs: SharedPreferences = activity!!.getSharedPreferences("EntryInfo", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        val closePrompt = ClosePromptFragment().newInstance()

        binding.buttonClose.setOnClickListener {

            closePrompt.show(activity!!.supportFragmentManager, "close_prompt")

        }

        viewModel.offers.observe(this, Observer {

            if (it != null) {

                val adapter = OfferAdapter(context!!, it)

                binding.offerList.adapter = adapter

                binding.offerList.setOnItemClickListener { parent, view, position, id ->

                    prefEditor
                        .putInt("offerId", viewModel.offers.value!![position].id!!)
                        .putString("startAfter", viewModel.offers.value!![position].startAfter)
                        .putInt("resultPrice", viewModel.offers.value!![position].price!!)
                        .putString("rentType", viewModel.offers.value!![position].daysType)
                        .apply()

                    view.findNavController().navigate(MonthlyOffersFragmentDirections.actionMonthlyOffersFragmentToMonthlyCalendarFragment())

                }

            }

        })

        return binding.root
    }



}

class OfferAdapter(private val context: Context, private val array: List<Offer>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = inflater.inflate(R.layout.monthly_offer_item, parent, false)

        view.name.text = array[position].daysType
        view.price.text = "${array[position].price} ₽/мес"
        view.startDay.text = "Аренда возможна с ${array[position].startAfter}"
        //view.dayCount.text = "${daysArray[position].days.size} д."

        return view
    }

    override fun getItem(position: Int): Any {
        return array[position]
    }

    override fun getItemId(position: Int): Long {
        return array[position].id!!.toLong()
    }

    override fun getCount(): Int {
        return array.size
    }
}

