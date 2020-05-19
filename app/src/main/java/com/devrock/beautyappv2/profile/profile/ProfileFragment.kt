package com.devrock.beautyappv2.profile.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.MainActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        val uri = "https://beauty.judoekb.ru/api/account/getPhoto?subject=%2B79121111160"

        val prefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        Glide.with(this)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.userpic_empty_ph)
            )
            .into(binding.profileAvatar)

        binding.button.setOnClickListener {

            prefEditor.clear().commit()
            activity!!.finish()

        }


/*
        val navBar = binding.bottomNavBar

        navBar.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_workplaces -> navBar.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWorkplacesFragment(session))
                R.id.action_search -> navBar.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMapFragment(session))
            }
        }*/



        return binding.root
    }

}