package com.devrock.beautyappv2.signup.name

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentNameBinding
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.findNavController


class NameFragment : Fragment() {

    private val viewModel: NameViewModel by lazy {
        ViewModelProviders.of(this).get(NameViewModel::class.java)
    }

    private lateinit var binding: FragmentNameBinding




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNameBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        val inputLastname: EditText = binding.signupLastnameInput
        val inputName: EditText = binding.signupNameInput
        val button: Button = binding.authButton
        var name = ""
        var lastName = ""
        val args = NameFragmentArgs.fromBundle(arguments!!)
        val session = args.session





        button.isEnabled = false
        button.isClickable = false

        viewModel.status.observe( this, Observer { v ->
            when (v) {
                "Ok" -> {
                    binding.authButton.findNavController().navigate(NameFragmentDirections.actionNameFragmentToUserpicFragment(session))
                }
            }
        })

        button.setTextColor((resources.getColor(R.color.colorButtonDisabled)))



        inputName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null) {
                    button.isEnabled = viewModel.setButtonAvailability(s.length, lastName.length)
                    button.isClickable = viewModel.setButtonAvailability(s.length, lastName.length)
                    if(button.isEnabled) button.setTextColor((resources.getColor(R.color.white)))
                    if(!button.isEnabled) button.setTextColor((resources.getColor(R.color.colorButtonDisabled)))
                    name = s.toString()
                }
            }
        })

        inputLastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null) {
                    button.isEnabled = viewModel.setButtonAvailability(name.length, s.length)
                    button.isClickable = viewModel.setButtonAvailability(name.length, s.length)
                    if(button.isEnabled) button.setTextColor((resources.getColor(R.color.white)))
                    if(!button.isEnabled) button.setTextColor((resources.getColor(R.color.colorButtonDisabled)))
                    lastName = s.toString()
                }
            }
        })


        binding.authButton.setOnClickListener {

            viewModel.accountCreate(session, name, lastName)


        }

        return binding.root
    }







}
