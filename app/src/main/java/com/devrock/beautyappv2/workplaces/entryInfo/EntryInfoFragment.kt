package com.devrock.beautyappv2.workplaces.entryInfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.databinding.FragmentEntryInfoBinding
import com.devrock.beautyappv2.net.MasterEntriesResponse
import com.devrock.beautyappv2.net.MasterEntry
import com.devrock.beautyappv2.util.formatTimeslots
import com.devrock.beautyappv2.util.getFormattedDate
import com.devrock.beautyappv2.util.getFormattedMonth
import com.devrock.beautyappv2.workplaces.WorkplacesFragmentDirections
import com.devrock.beautyappv2.workplaces.WorkplacesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_app.*

class EntryInfoFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(WorkplacesViewModel::class.java)
    }

    private lateinit var binding: FragmentEntryInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentEntryInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val entryId = EntryInfoFragmentArgs.fromBundle(arguments!!).entryId

        val locationPrefs = activity!!.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)

        val session = sessionPrefs.getString("session", "")
        val userLon = locationPrefs.getFloat("lon", 0f).toDouble()
        val userLat = locationPrefs.getFloat("lat", 0f).toDouble()


        viewModel.getCreatedMasterEntries(session!!)

        viewModel.createdMasterEntries.observe(this, Observer { list ->

            if (list != null) {
                val currentEntry = viewModel.createdMasterEntries.value!!.find { it.id == entryId }!!

                val formattedDate = getFormattedDate(currentEntry!!.startDate)


                binding.salonName.text = currentEntry.salon.name
                binding.salonRating.text = currentEntry.salon.rating.toString()
                binding.salonAddress.text = currentEntry.salon.geo.address
                binding.entryDate.text = formattedDate
                binding.entrySlots.text = formatTimeslots(currentEntry.timeSlots!!)
                binding.resultPrice.text = "${currentEntry.price} ₽"
                binding.hourPrice.text = "${currentEntry.price / currentEntry.timeSlots!!.size} ₽ / час"

                Picasso.get()
                    .load(currentEntry.salon.thumbnailPhoto)
                    .fit()
                    .into(binding.salonPhoto)

                binding.salonInfoButton.setOnClickListener {
                    it.findNavController().navigate(EntryInfoFragmentDirections.actionEntryInfoFragmentToSalonFragment(currentEntry.salon.id, session!!, userLon, userLat))
                }

                binding.buttonBack.setOnClickListener {
                    it.findNavController().navigate(EntryInfoFragmentDirections.actionEntryInfoFragmentToWorkplacesFragment(session!!))
                }

                binding.cancelButton.setOnClickListener {

                    viewModel.entryCancel(session!!, currentEntry.id)
                    it.findNavController().navigate(EntryInfoFragmentDirections.actionEntryInfoFragmentToWorkplacesFragment(session!!))

                }
            }

        })


        //val formattedTimeslots = formatTimeslots()
        activity!!.bottomNavBar.visibility = View.GONE








        return binding.root

    }
}