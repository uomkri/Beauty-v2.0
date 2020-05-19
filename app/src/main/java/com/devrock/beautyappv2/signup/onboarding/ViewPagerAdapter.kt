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

        when (position) {
            0 -> {
                caption1.text = "Найди студию рядом"
                caption2.text = "Проще поиска рабочего места поблизости не бывает"
                imageView.setImageResource(R.drawable.ic_onboarding1)
            }
            1 -> {
                caption1.text = "Получи подтверждение"
                caption2.text = "Салон подтвердит бронирование за считанные минуты"
                imageView.setImageResource(R.drawable.ic_onboarding2)
            }
            2 -> {
                caption1.text = "Работай!"
                caption2.text = "Идеальное рабочее место, когда и где вам удобно"
                imageView.setImageResource(R.drawable.ic_onboarding3)
            }
        }
    }

}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)