package com.devrock.beautyappv2.salon.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devrock.beautyappv2.R
import kotlinx.android.synthetic.main.fragment_info.view.*

class SalonViewPagerAdapter : RecyclerView.Adapter<SalonViewPagerAdapter.InfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder = InfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_info , parent, false))

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.view.textView4.text = "Page" + position.toString()
    }

    class InfoViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}

