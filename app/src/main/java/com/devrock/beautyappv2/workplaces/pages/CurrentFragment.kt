package com.devrock.beautyappv2.workplaces.pages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentCurrentBinding
import com.devrock.beautyappv2.workplaces.WorkplacesFragmentDirections
import com.devrock.beautyappv2.workplaces.WorkplacesViewModel
import com.github.underscore.U
import kotlinx.android.synthetic.main.workplaces_date_cluster.view.*

class CurrentFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(WorkplacesViewModel::class.java)
    }

    private lateinit var binding: FragmentCurrentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCurrentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val prefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val session = prefs.getString("session", null)

        viewModel.getMasterCurrentEntries(session!!)

        binding.findButton.setOnClickListener {

            it.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToMapFragment())

        }

        viewModel.createdMasterEntries.observe(this, Observer {

            if (it != null && it.isNotEmpty()) {
                binding.screenEmpty.visibility = View.GONE

                val sortedByDate = U.groupBy(it) { entry ->
                    return@groupBy entry.startDate
                }

                Log.e("itemm", sortedByDate.toString())

                for (item in sortedByDate) {

                    val containerView = inflater.inflate(R.layout.workplaces_date_cluster, null)

                    val insertPoint = containerView.dateClusterInsertPoint

                    Log.e("item", item.toString())

                }


            } else {
                binding.screenEmpty.visibility = View.VISIBLE
            }

        })


        return binding.root
    }
}