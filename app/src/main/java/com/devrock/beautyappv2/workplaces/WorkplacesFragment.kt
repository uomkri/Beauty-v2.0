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
import com.devrock.beautyappv2.workplaces.pages.WorkplacesFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_app.*

class WorkplacesFragment : Fragment() {

    private val viewModel: WorkplacesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(WorkplacesViewModel::class.java)
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

        binding.wpViewpager.adapter = WorkplacesFragmentStateAdapter(activity!!)

        TabLayoutMediator(binding.wpTabs, binding.wpViewpager) {tab, position ->
            when (position) {
                0 -> tab.text = "Текущие"
                1 -> tab.text = "Ожидает подтверждения"
                2 -> tab.text = "Заявки"
            }
        }.attach()

        activity!!.bottomNavBar.visibility = View.VISIBLE

        return binding.root
    }

}