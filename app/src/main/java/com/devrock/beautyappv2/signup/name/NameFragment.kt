package com.devrock.beautyappv2.signup.name

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.MainActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentNameBinding
import com.devrock.beautyappv2.ui.ColorTextInput
import com.devrock.beautyappv2.util.Prefs
import com.google.android.material.textfield.TextInputEditText
import java.io.InputStream
import android.Manifest
import android.widget.Toast

const val RESULT_LOAD_IMAGE: Int = 1
const val PERMISSION_REQUEST_CODE: Int = 2

class NameFragment : Fragment() {

    private val viewModel: NameViewModel by lazy {
        ViewModelProviders.of(this).get(NameViewModel::class.java)
    }

    private lateinit var binding: FragmentNameBinding
    lateinit var prefs: Prefs
    private var selectedImage: Uri? = null
    private var path: String? = ""




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

        binding.signupSetAvatar.setOnClickListener { getImage() }

        binding.authButton.setOnClickListener {

            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) viewModel.accountCreate(session, name, lastName, path)
                else Toast.makeText(context, "PERMISSION DENIED", Toast.LENGTH_SHORT)
            } else viewModel.accountCreate(session, name, lastName, path)


        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            selectedImage = data?.data
            path = viewModel.getPath(context!!, selectedImage!!)
            Log.i("PATH", path)

            Glide.with(this)
                .load(selectedImage)
                .apply(RequestOptions()
                .placeholder(R.drawable.pholder))
                .into(binding.signupSetAvatar)


        }
    }

    fun getImage() {

        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, RESULT_LOAD_IMAGE)
    }



}
