package com.devrock.beautyappv2.signup.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devrock.beautyappv2.R
import kotlinx.android.synthetic.main.onboarding_page.view.*

class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH = PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_page, parent, false))


    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        vptestText.text = "XYN${position}"
    }

}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)