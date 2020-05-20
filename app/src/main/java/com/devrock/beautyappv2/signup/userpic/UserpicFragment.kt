package com.devrock.beautyappv2.signup.userpic

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentUserpicBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File


const val RESULT_LOAD_IMAGE: Int = 1
const val PERMISSION_REQUEST_CODE: Int = 2
const val IMAGE_PICK: Int = 3

class UserpicFragment : Fragment() {

    private val viewModel: UserpicViewModel by lazy {
        ViewModelProviders.of(this).get(UserpicViewModel::class.java)
    }

    private lateinit var binding: FragmentUserpicBinding

    private var selectedImage: Uri? = null
    private var path: String = ""


    private var bitmap = MutableLiveData<Bitmap>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentUserpicBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        val args = UserpicFragmentArgs.fromBundle(arguments!!)
        val session = args.session

        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }

        binding.signupSetAvatar.setOnClickListener { getImage() }

        binding.authButton.setOnClickListener {
            it.findNavController().navigate(UserpicFragmentDirections.actionUserpicFragmentToOnboardingFragment(session))
        }

        return binding.root
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
            if (resultCode == RESULT_OK) {

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

                        viewModel.accountSetPhoto(
                            String(photo),
                            UserpicFragmentArgs.fromBundle(arguments!!).session
                        )
                    } else {
                        Toast.makeText(context!!, "NULL COMP", Toast.LENGTH_SHORT).show()
                    }
                })

                Glide.with(this)
                    .load(resultUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.userpic_empty_ph)
                    )
                    .into(binding.signupSetAvatar)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = result.error
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}