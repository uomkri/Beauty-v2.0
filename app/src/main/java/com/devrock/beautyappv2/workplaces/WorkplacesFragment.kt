package com.devrock.beautyappv2.workplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentWorkplacesBinding

class WorkplacesFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(this).get(WorkplacesViewModel::class.java)
    }

    private lateinit var binding: FragmentWorkplacesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentWorkplacesBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        //val args = WorkplacesFragmentArgs.fromBundle(arguments!!)
        //val session = args.session

/*
        val navBar = binding.bottomNavBar

        navBar.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_account -> navBar.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToProfileFragment(session))
                R.id.action_search -> navBar.findNavController().navigate(WorkplacesFragmentDirections.actionWorkplacesFragmentToMapFragment(session))
            }
        }*/



        return binding.root
    }

}