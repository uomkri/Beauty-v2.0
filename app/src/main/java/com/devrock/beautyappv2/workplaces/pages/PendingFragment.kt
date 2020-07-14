package com.devrock.beautyappv2.workplaces.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.databinding.FragmentPendingBinding
import com.devrock.beautyappv2.workplaces.WorkplacesFragmentDirections
import com.devrock.beautyappv2.workplaces.WorkplacesViewModel

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

        binding.findButton.setOnClickListener {

            it.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToMapFragment())

        }

        return binding.root
    }
}