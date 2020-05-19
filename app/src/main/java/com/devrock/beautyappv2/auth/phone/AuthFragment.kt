package com.devrock.beautyappv2.auth.phone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.auth.phone.AuthFragmentDirections
import com.devrock.beautyappv2.databinding.FragmentAuthBinding
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    private lateinit var binding: FragmentAuthBinding

    private var phone: String = ""

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
        binding = FragmentAuthBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        val input: EditText = binding.authPhoneInput
        val button: Button = binding.authButton

        val prefs: SharedPreferences = context!!.getSharedPreferences("Session", 0)

        viewModel.getLocalSession(prefs)

        viewModel.isScopeActive.observe(this, Observer {
            if(it == true) {
                binding.animView.visibility = View.VISIBLE
            }
            else {
                binding.animView.visibility = View.GONE
            }

        })

        viewModel.session.observe(this, Observer {
            if (it != null) {
                Log.e("SESS", it)
                viewModel.checkLogin()
            }
        })

        viewModel.isLoggedIn.observe(this, Observer {
            if (it == true) gotoAppActivity(viewModel.session.value!!)
        })

        binding.authPhone.isFocusableInTouchMode = true;

        button.isEnabled = false
        button.isClickable = false
        button.setTextColor((resources.getColor(R.color.primaryDk)))
        //input mask
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
                    if(!button.isEnabled) button.setTextColor((resources.getColor(R.color.primaryDk)))
                    phone = s.toString()
                }
            }
        })
        formatWatcher.installOn(input)

        input.setOnFocusChangeListener{v, b ->
            if(b) input.setText("+7 (")
        }



        viewModel.status.observe(this, Observer { newStatus ->
            val view : View = binding.authButton
            if(newStatus == "Ok") {
                view.findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToAuthPinFragment(phone)
            )}
            else Toast.makeText(context, newStatus, Toast.LENGTH_SHORT).show()
         })

        binding.authButton.setOnClickListener {
            viewModel.sendCode(phone)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.authPhone.clearFocus()
    }

    fun gotoAppActivity(session: String) {
        val intent = Intent(context, AppActivity::class.java)
        intent.putExtra("session", session)
        startActivity(intent)
    }



}