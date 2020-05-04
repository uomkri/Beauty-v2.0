package com.devrock.beautyappv2.auth.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.auth.pin.AuthPinFragmentArgs
import com.devrock.beautyappv2.databinding.FragmentAuthPinBinding
import com.devrock.beautyappv2.util.Prefs
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView

class AuthPinFragment : Fragment() {

    private val viewModel: AuthPinViewModel by lazy {
        ViewModelProviders.of(this).get(AuthPinViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthPinBinding

    var prefs: Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthPinBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        prefs = context?.let { Prefs(it) }
        viewModel.prefs = prefs

        val args = AuthPinFragmentArgs.fromBundle(arguments!!)
        val phone = args.phone

        val otp : OtpView = binding.otpView
        otp.setOtpCompletionListener(OnOtpCompletionListener() {
            viewModel.phoneConfirm(phone, it)
        })

        viewModel.registered.observe(this, Observer { v ->
            if(!v) binding.authHeader.findNavController().navigate(AuthPinFragmentDirections.actionAuthPinFragmentToNameFragment(viewModel.session.value.toString()))
            if(v) binding.authHeader.findNavController().navigate(AuthPinFragmentDirections.actionAuthPinFragmentToNameFragment(viewModel.session.value.toString()))
        })

        return binding.root
    }
}