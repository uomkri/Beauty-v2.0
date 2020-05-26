package com.devrock.beautyappv2.salon.pages

import android.app.ActionBar
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.net.toUri
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentPhotosBinding
import com.devrock.beautyappv2.salon.SalonViewModel
import com.google.android.flexbox.FlexboxLayout
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.photo_item.view.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devrock.beautyappv2.databinding.PhotoItemBinding
import com.devrock.beautyappv2.net.SalonPhoto
import com.devrock.beautyappv2.salon.SalonFragmentDirections
import com.github.underscore.lodash.U
import com.squareup.picasso.Picasso


class PhotosFragment : Fragment() {
    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentPhotosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPhotosBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val TABLE_COLUMNS = 3

        val PHOTOS_ENDPOINT = "https://beauty.judoekb.ru/api/salons/photo"
        val stock =
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg"

        val stockList = listOf (
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://s1.stc.all.kpcdn.net/putevoditel/projectid_103889/images/tild3836-3835-4236-b661-393338356563__beauty-salon-4043096.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg",
            "https://www.victoria-salon.ru/wp-content/uploads/2017/04/main_room3-1-1024x683.jpg"

        )

        var data = mutableListOf<SalonPhoto>()

        for ((index, item) in stockList.withIndex()) {
            data.add(SalonPhoto(index, item))
        }

        viewModel.photosGridList.value = data

        //binding.photosGrid.adapter = ImageAdapter(context!!, stockList)

        binding.photosGrid.adapter = ImageGridAdapter(viewModel)

        val adapter = binding.photosGrid.adapter as ImageGridAdapter


        viewModel.salonInfo.observe(this, Observer {
            if (it.photos.isNotEmpty()) {


                adapter.submitList(data)

            }
        })



        return binding.root
    }



    class ImageGridAdapter(val viewModel: SalonViewModel) : ListAdapter<SalonPhoto, ImageGridAdapter.ViewHolder>(DiffCallback) {

        companion object DiffCallback : DiffUtil.ItemCallback<SalonPhoto>() {
            override fun areItemsTheSame(oldItem: SalonPhoto, newItem: SalonPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SalonPhoto, newItem: SalonPhoto): Boolean {
                return oldItem.imgUrl == newItem.imgUrl
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ImageGridAdapter.ViewHolder {

            return ViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context)))
        }

        override fun onBindViewHolder(holder: ImageGridAdapter.ViewHolder, position: Int) {
            val image = getItem(position)
            holder.bind(image)

            holder.itemView.setOnClickListener {
                it.findNavController().navigate(SalonFragmentDirections.actionSalonFragmentToImageViewFragment(image.id, viewModel.salonInfo.value!!.info.id, viewModel.session.value!!, viewModel.salonInfo.value!!.info.geo.longitude, viewModel.salonInfo.value!!.info.geo.latitude))
            }

        }

        class ViewHolder(private var binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: SalonPhoto) {
                val view = binding.salonPhotoItem

                val transformation = jp.wasabeef.picasso.transformations.RoundedCornersTransformation(6, 0)

                Picasso.get()
                    .load(item.imgUrl)
                    .centerCrop()
                    .resize(104, 104)
                    .transform(transformation)
                    .into(view)

                binding.executePendingBindings()

            }
        }

    }

}
