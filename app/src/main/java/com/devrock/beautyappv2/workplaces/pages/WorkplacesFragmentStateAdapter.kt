package com.devrock.beautyappv2.workplaces.pages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkplacesFragmentStateAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CurrentFragment()
            }
            1 -> {
                PendingFragment()
            }
            2 -> {
                EntriesFragment()
            }
            else -> {
                CurrentFragment()
            }
        }
    }

}