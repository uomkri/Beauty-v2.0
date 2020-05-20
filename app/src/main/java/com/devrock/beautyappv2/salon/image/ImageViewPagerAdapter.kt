package com.devrock.beautyappv2.salon.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.net.SalonPhoto
import com.devrock.beautyappv2.signup.onboarding.PagerVH
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.imageview_pageritem.view.*

class ImageViewPagerAdapter(private val list: MutableList<SalonPhoto>) : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH = PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.imageview_pageritem, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {

        Picasso
            .get()
            .load(list[position].imgUrl)
            .into(this.zoomView)

        this.zoomView.setOnClickListener {  }
    }
}