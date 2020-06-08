package com.devrock.beautyappv2.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SplashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSplashBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)


        viewModel.getLocalSession(sessionPrefs)

        viewModel.session.observe(this, Observer {
            if (it != null) {
                Log.e("SESS", it)
                viewModel.checkLogin()
            } else {
                binding.root.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthFragment())
            }
        })

        viewModel.isLoggedIn.observe(this, Observer {

            when (it) {
                true -> gotoAppActivity(viewModel.session.value!!)
                false -> binding.root.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthFragment())
            }

        })

        viewModel.isLoggedIn.observe(this, Observer {

        })


        return binding.root

    }

    fun gotoAppActivity(session: String) {
        val intent = Intent(context, AppActivity::class.java)
        intent.putExtra("session", session)
        startActivity(intent)
    }

}