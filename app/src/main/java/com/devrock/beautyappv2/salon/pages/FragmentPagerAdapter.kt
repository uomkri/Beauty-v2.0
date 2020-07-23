package com.devrock.beautyappv2.salon.pages


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter



class SalonFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                InfoFragment()
            }
            1 -> {
                PricesFragment()
            }
            2 -> {
                PhotosFragment()
            }
            3 -> {
                FeedbackFragment()
            }
            else -> InfoFragment()
        }


}

