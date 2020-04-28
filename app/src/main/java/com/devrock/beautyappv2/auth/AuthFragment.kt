package com.devrock.beautyappv2.auth

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentAuthBinding
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        val input: EditText = binding.authPhoneInput
        val button: Button = binding.authButton
        button.isEnabled = false
        button.isClickable = false
        button.setTextColor((resources.getColor(R.color.colorButtonDisabled)))
        val slots = UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___ __ __")
        val formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null) {
                    button.isEnabled = viewModel.setButtonAvailability(s.length)
                    button.isClickable = viewModel.setButtonAvailability(s.length)
                    if(button.isEnabled) button.setTextColor((resources.getColor(R.color.white)))
                    if(!button.isEnabled) button.setTextColor((resources.getColor(R.color.colorButtonDisabled)))
                }
            }
        })
        formatWatcher.installOn(input)
        return binding.root
    }



}