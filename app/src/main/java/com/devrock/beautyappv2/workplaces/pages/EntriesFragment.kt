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
import com.devrock.beautyappv2.databinding.FragmentEntriesBinding
import com.devrock.beautyappv2.net.MasterEntry
import com.devrock.beautyappv2.workplaces.WorkplacesFragmentDirections
import com.devrock.beautyappv2.workplaces.WorkplacesViewModel
import com.github.underscore.U
import kotlinx.android.synthetic.main.entry_default.view.*
import kotlinx.android.synthetic.main.workplaces_date_cluster.view.*

class EntriesFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(WorkplacesViewModel::class.java)
    }

    private lateinit var binding: FragmentEntriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEntriesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val prefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val session = prefs.getString("session", null)

        viewModel.getCreatedMasterEntries(session!!)

        binding.findButton.setOnClickListener {

            it.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToMapFragment())

        }

        viewModel.createdMasterEntries.observe(this, Observer {

            if (it != null && it.isNotEmpty()) {
                binding.screenEmpty.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE

                val sortedByDate = U.groupBy(it) { entry ->
                    return@groupBy entry.startDate
                }

                Log.e("itemm", sortedByDate.toString())

                for (item in sortedByDate) {

                    val containerView = inflater.inflate(R.layout.workplaces_date_cluster, null)

                    val insertPoint = binding.scrollViewInsertPoint

                    containerView.dateClusterDate.text = item.key

                    containerView.dateClusterInsertPoint.adapter = EntriesAdapter(context!!, viewModel.createdMasterEntries.value!!)

                    insertPoint.addView(containerView)

                    Log.e("item", item.toString())

                    /*for (slot in item.value) {

                        val view = inflater.inflate(R.layout.entry_default, null)
                        view.slots.text = slot.timeSlots.toString()
                        view.salonName.text = slot.salon.name
                        view.salonAddress.text = slot.status

                        val containerInsertPoint = containerView.dateClusterInsertPoint

                        containerInsertPoint.addView(view)

                    }*/

                }


            } else {
                binding.screenEmpty.visibility = View.VISIBLE
            }

        })

        return binding.root
    }
}

class EntriesAdapter(var context: Context, list: List<MasterEntry>) : BaseAdapter() {

    lateinit var inflater: LayoutInflater
    var data: List<MasterEntry> = list

    lateinit var view: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = this.data[position]

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.entry_default, parent, false)

        view.slots.text = item.timeSlots.toString()
        view.salonName.text = item.salon.name
        view.salonAddress.text = item.status

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