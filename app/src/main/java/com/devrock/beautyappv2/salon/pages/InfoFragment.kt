package com.devrock.beautyappv2.salon.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devrock.beautyappv2.R
import kotlin.properties.Delegates

class InfoFragment : Fragment() {

    val ARG_PAGE: String = "ARG_PAGE"

    private var mPage by Delegates.notNull<Int>()

    fun newInstance(page: Int): InfoFragment {
        val args: Bundle = Bundle()
        args.putInt(ARG_PAGE, page)
        val fragment: InfoFragment = InfoFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mPage = arguments!!.getInt(ARG_PAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_info, container, false)
        return view
    }

}