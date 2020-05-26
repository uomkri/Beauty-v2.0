package com.devrock.beautyappv2.signup.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProviders.of(this).get(OnboardingViewModel::class.java)
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

        binding.dotsIndicator.setViewPager2(binding.obViewpager)

        val args = OnboardingFragmentArgs.fromBundle(arguments!!)
        val session = args.session

        binding.startButton.setOnClickListener {
            gotoAppActivity(session)
        }



        return binding.root
    }

    fun gotoAppActivity(session: String) {
        val intent = Intent(context, AppActivity::class.java)
        intent.putExtra("session", session)
        startActivity(intent)
    }
}