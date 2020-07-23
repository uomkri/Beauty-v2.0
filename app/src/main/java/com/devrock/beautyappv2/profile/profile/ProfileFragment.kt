package com.devrock.beautyappv2.profile.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.MainActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentProfileBinding
import com.devrock.beautyappv2.signup.userpic.UserpicFragmentArgs
import com.devrock.beautyappv2.signup.userpic.Utils
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.io.File
import java.lang.RuntimeException

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

        binding.editTextName.isEnabled = false
        binding.editTextPhone.isEnabled = false

        binding.buttonTestCrash.setOnClickListener {

            throw RuntimeException("TEST NAYOB PRODA")

        }

        binding.buttonEditName.setOnClickListener {

            binding.editTextName.isEnabled = true
            it.visibility = View.GONE
            binding.buttonConfirmName.visibility = View.VISIBLE

            binding.editTextName.requestFocus()

            showKeyboard(binding.editTextName)

        }

        binding.profileUserpic.setOnClickListener {

            val uri = "https://beauty.judoekb.ru/api/account/getPhoto?subject=%2B${viewModel.accountInfo.value!!.phone!!.drop(1)}&kek=${Math.random()}"
            Log.e("URI", uri)

            Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_userpic_placeholder)
                .fit()
                .into(binding.profileUserpic)

        }

        binding.buttonEditPhone.setOnClickListener {

            binding.editTextPhone.isEnabled = true
            it.visibility = View.GONE
            binding.buttonConfirmPhone.visibility = View.VISIBLE

            binding.editTextPhone.requestFocus()

            showKeyboard(binding.editTextPhone)

        }

        val prefs: SharedPreferences = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val prefEditor: SharedPreferences.Editor = prefs.edit()

        val slots = UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___ __ __")
        val formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.editTextPhone)

        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
        val session = sessionPrefs.getString("session", "")

        viewModel.getCurrentAccount(session!!)

        viewModel.accountInfo.observe(this, Observer {

            if (it != null) {

                binding.editTextName.text = SpannableStringBuilder("${it.name} ${it.surname}")
                binding.editTextPhone.text = SpannableStringBuilder("${it.phone}")

                val uri = "https://beauty.judoekb.ru/api/account/getPhoto?subject=%2B${viewModel.accountInfo.value!!.phone!!.drop(1)}&kek=${Math.random()}"
                Log.e("URI", uri)

                Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.ic_userpic_placeholder)
                    .fit()
                    .into(binding.profileUserpic)

            }

        })

        binding.buttonHelp.setOnClickListener {

            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHelpFragment())

        }

        binding.buttonLogout.setOnClickListener {

            prefEditor.clear().commit()

            val intent = Intent(activity!!, MainActivity::class.java)
            activity!!.startActivity(intent)

        }

        binding.buttonSetPhoto.setOnClickListener {

            this.getImage()

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

    fun showKeyboard(view: View) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.showSoftInput(view, 0)
    }

    fun View.hideKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(context!!, this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                val resultUri: Uri = result.uri

                val path = File(resultUri.path!!).absolutePath
                Log.i("path", File(path).exists().toString())

                viewModel.getCompressedImageAsync(File(path), context!!)

                viewModel.img.observe(this, Observer {

                    Log.i("COMP", it.toString())

                    if (it != null) {
                        val bytes = Utils.read(it)

                        Log.i("bytes", String(bytes))
                        val photo = Base64.encode(bytes, Base64.NO_WRAP)
                        Log.i("BASE64", String(photo))

                        val sessionPrefs = activity!!.getSharedPreferences("Session", Context.MODE_PRIVATE)
                        val session = sessionPrefs.getString("session", "")

                        viewModel.accountSetPhoto(
                            String(photo),
                            session!!
                        )
                    } else {
                        Toast.makeText(context!!, "NULL COMP", Toast.LENGTH_SHORT).show()
                    }
                })

                Picasso.get()
                    .load(resultUri)
                    .fit()
                    .into(binding.profileUserpic)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = result.error
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

}