package com.devrock.beautyappv2.salon.image

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.devrock.beautyappv2.databinding.FragmentImageviewBinding
import com.devrock.beautyappv2.salon.SalonViewModel

class ImageViewFragment : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentImageviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val position = ImageViewFragmentArgs.fromBundle(arguments!!).position
        Log.e("POS", position.toString())

        binding = FragmentImageviewBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        binding.buttonClose.setOnClickListener {
            fragmentManager!!.popBackStack()
            Log.e("BT", "CLOSE")
        }

        viewModel.photosGridList.observe(this, Observer {
            if (it != null) {
                binding.itemCounter.text = "${position + 1} из ${it.size}"

                binding.imageviewVp.adapter = ImageViewPagerAdapter(it)

                binding.imageviewVp.currentItem = position

                binding.imageviewVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.itemCounter.text = "${position + 1} из ${it.size}"
                    }
                })
            }
        })











        return binding.root
    }

}