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

        //binding.photosGrid.adapter = ImageAdapter(context!!, stockList)

        binding.photosGrid.adapter = ImageGridAdapter()

        val adapter = binding.photosGrid.adapter as ImageGridAdapter


        viewModel.salonInfo.observe(this, Observer {
            if (it.photos.isNotEmpty()) {


                adapter.submitList(data)


                //val insertPoint = binding.photosFlex

                //val TABLE_ROWS: Int



                //if (it.photos.size / TABLE_COLUMNS > 1) TABLE_ROWS =
                    //(it.photos.size / TABLE_COLUMNS) + 1
                //else TABLE_ROWS = 1

                //val photoCalc = inflater.inflate(R.layout.photo_item, null)

                /*for (i in 0..6) {
                    val photo = inflater.inflate(R.layout.photo_item, null)
                    photo.salon_photo_item.setImageResource(R.drawable.pholder)

                    val IMG_REL = 0.2875f

                    var viewWidth: Int

                    val display = activity!!.windowManager.defaultDisplay
                    val metrics = DisplayMetrics()
                    display.getMetrics(metrics)

                    val density = metrics.density

                    val displayWidth = metrics.widthPixels

                    Log.e("DISPLAY", displayWidth.toString())

                    var curRel: Float

                    var newWidth: Float

                    photo.post {
                        viewWidth = photo.width
                        Log.e("WIDTH", viewWidth.toString())


                        curRel = viewWidth.toFloat() / displayWidth.toFloat()

                        Log.e("REL", curRel.toString())

                        if (curRel > IMG_REL) {
                        while (curRel > IMG_REL) {
                                viewWidth -= 1
                                Log.e("NEWW", viewWidth.toString())
                                curRel = viewWidth.toFloat() / displayWidth.toFloat()

                                Log.i("REL1", curRel.toString())
                                Log.i("WID1", viewWidth.toString())

                            }
                        }
                        if (curRel < IMG_REL) {
                            while (curRel < IMG_REL) {
                                viewWidth += 1
                                Log.e("NEWW", viewWidth.toString())
                                curRel = viewWidth.toFloat() / displayWidth.toFloat()

                                Log.i("REL1", curRel.toString())
                                Log.i("WID1", viewWidth.toString())
                            }
                        }
                        Log.e("FINALW", viewWidth.toString())
                        Log.e("FINALR", curRel.toString())

                        photo.layoutParams = FlexboxLayout.LayoutParams(viewWidth, viewWidth)
                    }


                }*/





            }
        })



        return binding.root
    }

    /*class ImageAdapter : BaseAdapter {

        var list = ArrayList<String>()
        var context: Context? = null

        constructor(context: Context, list: ArrayList<String>) : super() {
            this.context = context
            this.list = list
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = this.list[position]

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            var view = inflater.inflate(R.layout.photo_item, null)

            GlideApp.with(context!!).load(item)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(6, 0)))
                .into(view.salon_photo_item)

            return view
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }
    }*/

    class ImageGridAdapter : ListAdapter<SalonPhoto, ImageGridAdapter.ViewHolder>(DiffCallback) {

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
            holder.bind(image.imgUrl)

        }

        class ViewHolder(private var binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: String) {
                val view = binding.salonPhotoItem

                Picasso.get()
                    .load(item)
                    .into(view)

                binding.executePendingBindings()

            }
        }

    }

}

@GlideModule
class GlideApp1 : AppGlideModule() {

}