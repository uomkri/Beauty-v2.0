package com.devrock.beautyappv2.profile.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.devrock.beautyappv2.MainActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentProfileBinding
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

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

        binding.buttonEditName.setOnClickListener {

            binding.editTextName.isEnabled = true
            it.visibility = View.GONE
            binding.buttonConfirmName.visibility = View.VISIBLE

            binding.editTextName.requestFocus()

            showKeyboard(binding.editTextName)

        }

        binding.buttonEditPhone.setOnClickListener {

            binding.editTextPhone.isEnabled = true
            it.visibility = View.GONE
            binding.buttonConfirmPhone.visibility = View.VISIBLE

            binding.editTextPhone.requestFocus()

            showKeyboard(binding.editTextPhone)

        }

        val uri = "https://beauty.judoekb.ru/api/account/getPhoto?subject=%2B79121111160"

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

            }

        })

        binding.buttonHelp.setOnClickListener {

            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHelpFragment())

        }



        Glide.with(this)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.userpic_empty_ph)
            )
            .into(binding.profileUserpic)

        binding.buttonLogout.setOnClickListener {

            prefEditor.clear().commit()
            activity!!.finish()

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

}