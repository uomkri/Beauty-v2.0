package com.devrock.beautyappv2.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.databinding.FragmentAuthBinding
import com.devrock.beautyappv2.databinding.FragmentAuthPinBinding

class AuthPinFragment : Fragment() {

    private val viewModel: AuthPinViewModel by lazy {
        ViewModelProviders.of(this).get(AuthPinViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthPinBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthPinBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
    }
}