package com.devrock.beautyappv2.signup.name

import android.app.AlertDialog
import android.content.DialogInterface
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
import android.util.Log
import android.widget.Toast
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

            val regex= Regex("\\s")
            val formName = regex.replace(name, "")
            val formLastName = regex.replace(lastName, "")
            val nameReg = Regex("[А-Я][а-я]+")
            Log.e("REGEX", "${nameReg.matches(formName)} ${nameReg.matches(formLastName)}")
            if (nameReg.matches(formName) && nameReg.matches(formLastName)) {
                viewModel.accountCreate(session, name, lastName)
            } else {
                if(!nameReg.matches(formName) && nameReg.matches(formLastName)) showAlert("Введено невалидное имя ")
                if(!nameReg.matches(formLastName) && nameReg.matches(formName)) showAlert("Введена невалидная фамилия ")
                if(!nameReg.matches(formLastName) && !nameReg.matches(formName)) showAlert("Введены невалидные имя и фамилия")
                Log.e("REGEX", "called alert")
            }
        }

        return binding.root
    }

    fun showAlert(message: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = alertButtonClick))
        builder.show()
    }

    private val alertButtonClick = { dialog: DialogInterface, which: Int -> }



}
