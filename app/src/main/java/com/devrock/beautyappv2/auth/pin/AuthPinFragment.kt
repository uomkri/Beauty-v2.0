package com.devrock.beautyappv2.auth.pin

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.auth.pin.AuthPinFragmentArgs
import com.devrock.beautyappv2.auth.pin.AuthPinViewModel.Companion.DONE
import com.devrock.beautyappv2.databinding.FragmentAuthPinBinding
import com.devrock.beautyappv2.util.Prefs
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView
import kotlinx.android.synthetic.main.fragment_auth_pin.view.*

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

        viewModel.currentTime.observe(this, Observer {v ->
            binding.pinCaption.text = "Получить новый код через ${DateUtils.formatElapsedTime(v)}"
            if(v == DONE) {
                binding.pinCaption.visibility = View.GONE
                binding.pinCaptionNew.visibility = View.VISIBLE
            }
        })

        binding.pinCaptionNew.setOnClickListener {
            viewModel.startTimer()
            binding.pinCaptionNew.visibility = View.GONE
            binding.pinCaption.visibility = View.VISIBLE
        }

        return binding.root
    }


}