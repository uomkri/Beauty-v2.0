package com.devrock.beautyappv2.auth.pin

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.auth.pin.AuthPinFragmentArgs
import com.devrock.beautyappv2.auth.pin.AuthPinViewModel.Companion.DONE
import com.devrock.beautyappv2.databinding.FragmentAuthPinBinding
import kotlinx.android.synthetic.main.fragment_auth_pin.view.*

class AuthPinFragment : Fragment() {

    private val viewModel: AuthPinViewModel by lazy {
        ViewModelProviders.of(this).get(AuthPinViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthPinBinding

    private lateinit var otpTextView: OtpTextView

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthPinBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel


        val args = AuthPinFragmentArgs.fromBundle(arguments!!)
        val phone = args.phone

        otpTextView = binding.otpView

        otpTextView.otpListener = object : OTPListener {
            override fun onOTPComplete(otp: String) {
                viewModel.phoneConfirm(phone, otp)
            }

            override fun onInteractionListener() {
            }
        }


        viewModel.status.observe(this, Observer { v ->
            if(v == "Error"){
                viewModel.errorText.observe( this, Observer { v ->
                    when (v) {
                        "Некорректный код" -> {
                            otpTextView.showError()
                            binding.pinError.text = "Введен неверный код. Попробуйте снова"
                            binding.pinError.visibility = View.VISIBLE
                        }
                        "Время до повторой отправки кода еще не прошло" -> {
                            otpTextView.showError()
                            binding.pinError.text = "Вы произвели слишком много попыток ввести неправильный код, подождите некоторое время"
                            binding.pinError.visibility = View.VISIBLE
                        }
                    }

                })
            } else if (v == "Ok"){
                otpTextView.showSuccess()
                binding.pinError.visibility = View.GONE
            }
        })

        viewModel.registered.observe(this, Observer { v ->
            if(!v) {
                binding.root.hideKeyboard()
                binding.authHeader.findNavController().navigate(AuthPinFragmentDirections.actionAuthPinFragmentToNameFragment(viewModel.session.value.toString()))
            }
            if(v) {
                binding.root.hideKeyboard()
                binding.authHeader.findNavController().navigate(AuthPinFragmentDirections.actionAuthPinFragmentToMapFragment(viewModel.session.value.toString()))
            }
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