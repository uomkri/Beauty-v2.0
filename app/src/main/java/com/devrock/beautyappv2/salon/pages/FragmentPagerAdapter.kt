package com.devrock.beautyappv2.salon.pages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.net.SalonByIdPayload
import kotlinx.android.synthetic.main.fragment_info.view.*

/*class SalonViewPagerAdapter() : RecyclerView.Adapter<SalonViewPagerAdapter.InfoViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder = InfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_info , parent, false))

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {

        when (position) {
            0 -> {
                holder.view.infoPage0.visibility = View.VISIBLE
                holder.view.infoPage1.visibility = View.GONE
            }
            1 -> {
                holder.view.infoPage0.visibility = View.GONE
                holder.view.infoPage1.visibility = View.VISIBLE
            }
        }
    }

    class InfoViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}*/

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

