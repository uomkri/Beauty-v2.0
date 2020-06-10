package com.devrock.beautyappv2.signup.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.databinding.FragmentOnboardingBinding
import com.devrock.beautyappv2.ui.DepthPageTransformer

class OnboardingFragment : Fragment() {

    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProviders.of(activity!!).get(OnboardingViewModel::class.java)
    }

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentOnboardingBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        binding.obViewpager.adapter = ViewPagerAdapter()
        binding.obViewpager.setPageTransformer(DepthPageTransformer() as ViewPager2.PageTransformer)

        binding.dotsIndicator.setViewPager2(binding.obViewpager)

        val args = OnboardingFragmentArgs.fromBundle(arguments!!)
        val session = args.session

        binding.startButton.setOnClickListener {
            gotoAppActivity(session)
        }

        binding.nextButton.setOnClickListener {
            gotoAppActivity(session)
        }

        activity!!.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        viewModel.isFinalPage.observe(this, Observer {

            if (it == true) {
                binding.nextButton.visibility = View.VISIBLE
                //binding.startButton.visibility = View.GONE
            } else {
                binding.nextButton.visibility = View.GONE
                //binding.startButton.visibility = View.VISIBLE
            }

        })

        binding.obViewpager.registerOnPageChangeCallback( object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.checkFinalPage(position)
            }
        })

        return binding.root
    }

    fun gotoAppActivity(session: String) {
        val intent = Intent(context, AppActivity::class.java)
        intent.putExtra("session", session)
        startActivity(intent)
    }
}