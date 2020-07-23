package com.devrock.beautyappv2.workplaces.pages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentPendingBinding
import com.devrock.beautyappv2.net.MasterEntry
import com.devrock.beautyappv2.util.formatTimeslots
import com.devrock.beautyappv2.util.getFormattedDate
import com.devrock.beautyappv2.workplaces.WorkplacesFragmentDirections
import com.devrock.beautyappv2.workplaces.WorkplacesViewModel
import com.github.underscore.U
import kotlinx.android.synthetic.main.entry_default.view.*
import kotlinx.android.synthetic.main.entry_default.view.salonName
import kotlinx.android.synthetic.main.entry_default.view.slots
import kotlinx.android.synthetic.main.entry_pending.view.*
import kotlinx.android.synthetic.main.workplaces_date_cluster.view.*

class PendingFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(WorkplacesViewModel::class.java)
    }

    private lateinit var binding: FragmentPendingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPendingBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val prefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val session = prefs.getString("session", null)


        viewModel.getPendingMasterEntries(session!!)

        binding.findButton.setOnClickListener {

            it.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToMapFragment())

        }

        viewModel.pendingMasterEntries.observe(this, Observer {

            if (it != null && it.isNotEmpty()) {
                binding.screenEmpty.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE

                val sortedByDate = U.groupBy(it) { entry ->
                    return@groupBy entry.startDate
                }

                Log.e("itemm", sortedByDate.toString())
                Log.i("size", sortedByDate.size.toString())

                for (item in sortedByDate) {

                    val containerView = inflater.inflate(R.layout.workplaces_date_cluster, null)

                    val insertPoint = binding.scrollViewInsertPoint

                    containerView.dateClusterDate.text = getFormattedDate(item.key)

                    containerView.dateClusterInsertPoint.adapter = PendingEntriesAdapter(context!!, item.value, viewModel, session)

                    insertPoint.addView(containerView)

                    Log.e("item", item.toString())

                }


            } else {
                binding.screenEmpty.visibility = View.VISIBLE
            }

        })

        return binding.root
    }
}

class PendingEntriesAdapter(var context: Context, list: List<MasterEntry>, val viewModel: WorkplacesViewModel, val session: String) : BaseAdapter() {

    lateinit var inflater: LayoutInflater
    var data: List<MasterEntry> = list

    lateinit var view: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = this.data[position]

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.entry_pending, parent, false)

        view.setOnClickListener {
            view.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToEntryInfoFragment(item.id))
        }

        /*val formattedSlots = item.timeSlots?.map {
            return@map "${it.start?.dropLast(3)}-${it.end?.dropLast(3)}"
        }*/

        val formattedSlots = formatTimeslots(item.timeSlots!!)

        view.slots.text = formattedSlots
        view.salonName.text = item.salon.name
        view.salonAddress.text = viewModel.getFormattedEntryStatus(item.status)

        view.confirmButton.setOnClickListener {

            viewModel.entryConfirm(session, item.id)

        }

        view.cancelButton.setOnClickListener {

            viewModel.entryCancel(session, item.id)

        }

        return view
    }

    override fun getItem(position: Int): MasterEntry {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}