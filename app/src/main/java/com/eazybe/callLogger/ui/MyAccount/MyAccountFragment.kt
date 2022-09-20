package com.eazybe.callLogger.ui.MyAccount

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.datastore.DataStoreConfiguration
import com.amplifyframework.datastore.generated.model.CallLogs
import com.amplifyframework.datastore.generated.model.Chatter
import com.amplifyframework.datastore.generated.model.User
import com.eazybe.callLogger.R
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.databinding.MyAccountFragmentBinding
import com.eazybe.callLogger.extensions.addRightDrawable
import com.eazybe.callLogger.extensions.getProgressDrawable
import com.eazybe.callLogger.extensions.loadImage
import com.eazybe.callLogger.extensions.toast
import com.eazybe.callLogger.ui.Plans.PlansFragment
import com.eazybe.callLogger.ui.SignUpAndLogin.Onboarding.OnboardingFragmentNameDirections
import com.eazybe.callLogger.ui.SignUpAndLogin.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.my_account_fragment.*

@AndroidEntryPoint
class MyAccountFragment : Fragment() {

    private lateinit var binding: MyAccountFragmentBinding
    private val viewModel: MyAccountViewModel by viewModels()
    private var userDataWS : WorkspaceDetails? = null

    private var syncState: String? = null
    private var syncStateWhatsApp: String? = null
    private var syncTimeUnpaid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPreferenceData()
    }

    private fun observeViewModel() {
        viewModel.userData.observe({ lifecycle }) { userData ->
        binding.apply {
            userDataWS = userData
            personName.text = userData.name?: "Unknown"
            personEmail.text = userData.email?: "No email provided"
            orgName.text = "${userData.orgName?: "No organization."}"
            personImage.loadImage(userData.profilePic, getProgressDrawable(requireContext()))
        }
        }

        viewModel.apply {

            callExpiry.observe({lifecycle}){
                syncState = it
                when(it){
                    "Trial_Expired" ->{
                        binding.timeLeftTxt.text = "Trial Expired"
//                    binding.timeLeftTxt.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.color_expired))
                        binding.timeLeftTxt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.color_expired))
                        binding.timeLeftTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_expired))
                        binding.timeLeftTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_call_icon), null,
                            ContextCompat.getDrawable(requireContext(),R.drawable.ic_not_syncing),null)
                        binding.timeLeftTxt.compoundDrawablePadding = 10
                    }
                    "Paid_Expired" ->{
                        binding.timeLeftTxt.text = "Plan Expired"
                        binding.timeLeftTxt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.color_expired))
                        binding.timeLeftTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_expired))
                        binding.timeLeftTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_call_icon), null,
                            ContextCompat.getDrawable(requireContext(),R.drawable.ic_not_syncing),null)
                        binding.timeLeftTxt.compoundDrawablePadding = 10
                    }
                    "Syncing" ->{
                        binding.timeLeftTxt.text = "Syncing"
                        binding.timeLeftTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_call_icon), null,
                            ContextCompat.getDrawable(requireContext(),R.drawable.baseline_sync),null)
                        binding.timeLeftTxt.compoundDrawablePadding = 10
                    }
                    "Trial_Active" ->{
                    callExpiryTime.observe({lifecycle}){ days ->
                        binding.timeLeftTxt.text = "$days Days Left"
                        syncTimeUnpaid = days
                        binding.timeLeftTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_call_icon), null,
                            ContextCompat.getDrawable(requireContext(),R.drawable.baseline_sync),null)
                        binding.timeLeftTxt.compoundDrawablePadding = 8
                    }
                    }
                    "Not_Started"->{
                        binding.timeLeftTxt.text = "Not Started"
                        binding.timeLeftTxt.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.grey_light))
                        binding.timeLeftTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.green_light))
                        binding.timeLeftTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_call_icon), null,
                            ContextCompat.getDrawable(requireContext(),R.drawable.ic_not_syncing),null)
                        binding.timeLeftTxt.compoundDrawablePadding = 8
                    }
                }

            }

            whatsAppExpiry.observe({lifecycle}){
                syncStateWhatsApp = it
                if (it == "Expired"){
                    binding.timeLeftTxtWhatsApp.text = "Not Started"
                    binding.timeLeftTxtWhatsApp.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.grey_light))
                    binding.timeLeftTxtWhatsApp.setTextColor(ContextCompat.getColor(requireContext(),R.color.green_light))

                    binding.timeLeftTxtWhatsApp.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.ic_whatsapp), null,
                        ContextCompat.getDrawable(requireContext(),R.drawable.ic_not_syncing),null)
                    binding.timeLeftTxtWhatsApp.compoundDrawablePadding = 8
                }else{
                    binding.timeLeftTxtWhatsApp.text = it
                }


            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MyAccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llLogout.setOnClickListener {
            viewModel.clearDataFromPreference()

            Amplify.DataStore.clear({
                val intent = Intent(requireContext(), RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            },{
                Log.e("MyAmplifyApp", "Error clearing DataStore", it)
                requireContext().toast("Something Went Wrong.")
            })
        }

        binding.llGetInTouch.setOnClickListener {
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

        binding.cLTopProfile.setOnClickListener {
            val action =
                MyAccountFragmentDirections.actionMyAccountFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }

        binding.llQuickReply.setOnClickListener {
            val action =
                MyAccountFragmentDirections.actionMyAccountFragmentToQuickRepliesFragment()
            findNavController().navigate(action)
        }

        binding.timeLeftTxt.setOnClickListener {
            val dialog = PlansFragment()
        when(syncState){
            "Trial_Expired" ->{
                val bundle = bundleOf("title" to "Trial_Expired")
                dialog.arguments = bundle
            }
            "Paid_Expired" ->{
                val bundle = bundleOf("title" to "Paid_Expired")
                dialog.arguments = bundle
            }
            "Syncing" ->{
                val bundle = bundleOf("title" to "Syncing")
                dialog.arguments = bundle
            }
            "Trial_Active" ->{
                val bundle = bundleOf("title" to "Trial_Active",
                    "days" to syncTimeUnpaid)
                dialog.arguments = bundle
            }
        }
            dialog.show(parentFragmentManager, "permission_fragment")
        }

        binding.timeLeftTxtWhatsApp.setOnClickListener {
            val dialog = PlansFragment()
            when(syncStateWhatsApp){
                "Expired" ->{
                    val bundle = bundleOf("title" to "Whatsapp_Expired")
                    dialog.arguments = bundle
                }
            }
            dialog.show(parentFragmentManager, "permission_fragment")
        }
    }
}