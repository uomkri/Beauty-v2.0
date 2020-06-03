package com.devrock.beautyappv2.rent

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.devrock.beautyappv2.BottomNavigationDirections
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.map.BottomDialogFragment
import com.devrock.beautyappv2.map.MapFragment
import kotlinx.android.synthetic.main.fragment_salon.*
import kotlinx.android.synthetic.main.rent_close_prompt.view.*

class ClosePromptFragment : DialogFragment() {

    fun newInstance(): ClosePromptFragment {
        return ClosePromptFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.rent_close_prompt, container, false)





        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }


        view.returnButton.setOnClickListener {
            this.dismiss()
        }

        view.proceedButton.setOnClickListener {

            this.dismiss()

            val navController = Navigation.findNavController(activity!!, R.id.bottom_nav_host_fragment)

            navController.navigate(ClosePromptFragmentDirections.actionGlobalClosePromptFragment())

        }

        return view
    }


}