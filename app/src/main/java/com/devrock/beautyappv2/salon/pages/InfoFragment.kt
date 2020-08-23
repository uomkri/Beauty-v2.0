package com.devrock.beautyappv2.salon.pages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentInfoBinding
import com.devrock.beautyappv2.salon.SalonViewModel
import com.devrock.beautyappv2.net.SalonByIdPayload
import com.devrock.beautyappv2.salon.SalonFragmentArgs
import com.devrock.beautyappv2.signup.userpic.PERMISSION_REQUEST_CODE
import kotlinx.android.synthetic.main.contacts_item.view.*
import kotlinx.android.synthetic.main.schedule_item.view.*
import kotlinx.android.synthetic.main.services_item.view.*
import kotlin.properties.Delegates

class InfoFragment() : Fragment() {

    private val viewModel: SalonViewModel by lazy {
        ViewModelProviders.of(activity!!).get(SalonViewModel::class.java)
    }

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentInfoBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater




        viewModel.salonInfo.observe(this, Observer {
            Log.e("VP", viewModel.salonInfo.value.toString())
            if (it != null) {
                binding.salonAddress.text = it.info.geo.address
                val schedule = viewModel.formatSchedule(it.salonSchedule)
                for (item in schedule) run {
                    val insertPoint = binding.scheduleContainer

                    val scheduleItem = inflater.inflate(R.layout.schedule_item, null)

                    if (insertPoint.childCount != 0) {
                        insertPoint.removeAllViews()
                    }

                    scheduleItem.scheduleText.text = item
                    insertPoint.addView(scheduleItem)
                }
                val contacts = it.contacts
                for (item in contacts) {
                    val contactsItem = inflater.inflate(R.layout.contacts_item, null)
                    val insertPoint = binding.contactsContainer

                    /*if (insertPoint.childCount != 0) {
                        insertPoint.removeAllViews()
                    }*/

                    contactsItem.contactsValue.text = item.value
                    when (item.contactType) {
                        "Telegram" -> {
                            contactsItem.iconPhone.visibility = View.GONE
                            contactsItem.iconWhatsapp.visibility = View.GONE
                            contactsItem.iconTelegram.visibility = View.VISIBLE
                        }
                        "WhatsApp" -> {
                            contactsItem.iconPhone.visibility = View.GONE
                            contactsItem.iconWhatsapp.visibility = View.VISIBLE
                            contactsItem.iconTelegram.visibility = View.GONE
                        }
                        "Phone" -> {
                            contactsItem.iconPhone.visibility = View.VISIBLE
                            contactsItem.iconWhatsapp.visibility = View.GONE
                            contactsItem.iconTelegram.visibility = View.GONE
                        }
                    }
                    insertPoint.addView(contactsItem)

                    contactsItem.setOnClickListener {
                        when (item.contactType) {
                            "Telegram" -> {
                                val tg = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${item.value.drop(1)}"))
                                tg.setPackage("org.telegram.messenger")
                                startActivity(tg)
                            }
                            "WhatsApp" -> {
                                val wa = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${item.value}"))
                                wa.setPackage("com.whatsapp")
                                startActivity(wa)
                            }
                            "Phone" -> {
                                if (ContextCompat.checkSelfPermission(
                                        context!!,
                                        Manifest.permission.CALL_PHONE
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    requestPermissions(
                                        arrayOf(Manifest.permission.CALL_PHONE),
                                        PERMISSION_REQUEST_CODE
                                    )

                                } else {
                                    val ph = Intent(Intent.ACTION_CALL, Uri.parse("tel:${item.value}"))
                                    startActivity(ph)
                                }
                            }
                        }
                    }
                }

                val services = it.additionalServices

                for (item in services!!) {

                    val view = inflater.inflate(R.layout.services_item, null)

                    view.serviceText.text = "- ${item.title}"

                    when (item.serviceType) {

                        "Free" -> binding.includedServices.addView(view)
                        "Paid" -> binding.paidServices.addView(view)

                    }

                }

            }
        })
    }

}