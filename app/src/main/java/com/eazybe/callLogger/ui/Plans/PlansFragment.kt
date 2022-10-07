package com.eazybe.callLogger.ui.Plans

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.PlansFragmentBinding
import com.eazybe.callLogger.extensions.colorizeText

class PlansFragment : DialogFragment() {

    private lateinit var binding: PlansFragmentBinding
    private var title: String? = null
    private var timeLeft: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlansFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val mArgs = arguments
            title = mArgs?.getString("title")
            timeLeft = mArgs?.getString("days")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialog?.setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        when (title) {
            "Trial_Active" -> {
                binding.syncText.text = "Syncing ~ $timeLeft days left "
                binding.plansText.text = "The data can be synced to your workspace only for the next $timeLeft days. " +
                        "Please upgrade to avoid losing data "
            }
            "Trial_Expired" -> {
                binding.lLsyncing.visibility = View.GONE
                binding.lLNotSyncing.visibility = View.VISIBLE
                binding.plansText.text = "Your trial has expired to continue syncing calls, messages and reports. Please upgrade. "

            }
            "Paid_Expired" -> {
                binding.lLsyncing.visibility = View.GONE
                binding.lLNotSyncing.visibility = View.VISIBLE
                binding.plansText.text = "Your trial has expired to continue syncing calls, messages and reports. Please upgrade. "
            }
            "Syncing" -> {
                binding.syncText.text = "Syncing... "
                binding.plansText.text = "The data can be synced to your workspace."
            }
            "Whatsapp_Expired" ->{
                binding.lLsyncing.visibility = View.GONE
                binding.lLNotSyncing.visibility = View.VISIBLE
                binding.plansText.text = "Sync has not been started. Please upgrade. "
                binding.notSyncText.text = "Sync not started... "
            }
            "Not_Synced" ->{
                binding.lLsyncing.visibility = View.GONE
                binding.lLNotSyncing.visibility = View.VISIBLE
                binding.plansText.text = "Sync has not been started. Please upgrade. "
                binding.notSyncText.text = "Sync not started... "
            }
        }
    }

    private fun setOnClickListener() {
        binding.apply {
            contactUsText.text = "Need Help? Contact us".colorizeText(
                "Contact us",
                ContextCompat.getColor(requireContext(), R.color.purple))

            billingText.text = "Please visit workspace.eazybe.com to upgrade".colorizeText(
                "workspace.eazybe.com",
                ContextCompat.getColor(requireContext(), R.color.purple))

            imgCross.setOnClickListener {
                dismiss()
            }
            contactUsText.setOnClickListener {
                val phoneNumber = "919818215182"
                val url = "https://api.whatsapp.com/send?phone=" + "+" + "${phoneNumber}&text=I am using Eazybe Mobile Application and I had some queries."
                try {

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        this.data = Uri.parse(url)
                        this.`package` = "com.whatsapp"
                    }
                    try {
                        startActivity(intent)
                    } catch (ex : ActivityNotFoundException){
                        Toast.makeText(
                            requireContext(),
                            "Whatsapp is not installed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(
                        requireContext(),
                        "Whatsapp is not installed in your phone.",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace()
                }
            }

        }
    }

}