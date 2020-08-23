package com.devrock.beautyappv2.profile.help

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    private val viewModel: HelpViewModel by lazy {
        ViewModelProviders.of(this).get(HelpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHelpBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.initQs()

        activity!!.window.apply {

            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.WHITE

        }


        binding.buttonBack.setOnClickListener {

            activity!!.supportFragmentManager.popBackStack()

        }

        viewModel.q1isOpen.observe(this, Observer {

            if (it != null) {

                when (it) {

                    false -> binding.q1Arrow.setImageResource(R.drawable.ic_down)
                    true -> binding.q1Arrow.setImageResource(R.drawable.ic_up)

                }

            }

        })

        viewModel.q2isOpen.observe(this, Observer {

            if (it != null) {

                when (it) {

                    false -> binding.q2Arrow.setImageResource(R.drawable.ic_down)
                    true -> binding.q2Arrow.setImageResource(R.drawable.ic_up)

                }

            }

        })

        binding.q1Header.setOnClickListener {

            when (viewModel.q1isOpen.value) {

                false -> expand(binding.q1)
                true -> collapse(binding.q1)

            }

            viewModel.q1ChangeStatus()

        }

        binding.q2Header.setOnClickListener {

            when (viewModel.q2isOpen.value) {

                false -> expand(binding.q2)
                true -> collapse(binding.q2)

            }

            viewModel.q2ChangeStatus()

        }



        return binding.root
    }

}