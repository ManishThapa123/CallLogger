package com.eazybe.callLogger.ui.SignUpAndLogin.Old

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eazybe.callLogger.BaseActivity
import com.eazybe.callLogger.databinding.SingUpFragmentBinding
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.ui.SignUpAndLogin.RegistrationAndLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: SingUpFragmentBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()
    private var simCardOneName: String? = ""
    private var simCardTwoName: String? = ""
    private var simCardCount: Int? = 0
    private var subscriptionInfoTelecom: ArrayList<String> = ArrayList()
    private var subscriptionInfoSub: ArrayList<String> = ArrayList()
    private var selectedSIM: Int = 1
    private var orgCode: Int? = null

    private val requestReadCallLogsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                //Get the call logs.
                registrationAndLoginViewModel.getSimCardInfos(requireActivity())
                observeViewModel()
            } else {
                //close the app
                activity?.onBackPressed()
            }
        }

    private val requestManageCallsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            //check if the call logs permission is accepted.
            checkCallLogsPermission()
        } else {
            //close the app
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = SingUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                when (selectedSIM) {
                    2 -> {
                        binding.simOneCarrier.visibility = View.VISIBLE
                        binding.simOneImage.visibility = View.VISIBLE
                        binding.simTwoCarrier.visibility = View.GONE
                        binding.simTwoImage.visibility = View.GONE
                        selectedSIM = 1
                        binding.llShowDualSim.visibility = View.VISIBLE
                    }
                    else -> {
                        activity?.onBackPressed()
                    }
                }

            }
        })
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Check if the call logs permission is accepted.
            checkCallLogsPermission()
        } else {
            //Ask for the Permission
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE)
        }
    }

    private fun checkCallLogsPermission() {
        //Check if the call logs permission is accepted.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {

            registrationAndLoginViewModel.getSimCardInfos(requireActivity())
            observeViewModel()
        } else {
            //Ask for the Read Call Logs Permission.
            requestReadCallLogsPermission.launch(
                Manifest.permission.READ_CALL_LOG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // attach mobile number field to country code field
        binding.ccp.registerCarrierNumberEditText(binding.clientNumber)
        setOnClickListeners()
        checkValidationForOrganizationCode()

    }

    private fun checkSIMCardStatus(simCardInfo: ArrayList<SubscriptionInfo>) {
        if (simCardCount == 1) {
            selectedSIM = 1
            simCardOneName = "${simCardInfo[0].carrierName}"
            Log.d("Sim Card", simCardCount.toString())
            binding.simOneCarrier.text = simCardOneName
        } else if (simCardCount == 2) {
            binding.llShowDualSim.visibility = View.VISIBLE
            simCardOneName = "${simCardInfo[0].carrierName}"
            simCardTwoName = "${simCardInfo[1].carrierName}"
            binding.simOneCarrier.text = simCardOneName
            binding.simTwoCarrier.text = simCardTwoName
            Log.d("Sim Card", simCardCount.toString())
        }

        binding.btnSkipSimOne.setOnClickListener {
            selectedSIM = 2
            binding.simOneCarrier.visibility = View.GONE
            binding.simOneImage.visibility = View.GONE
            binding.simTwoCarrier.visibility = View.VISIBLE
            binding.simTwoImage.visibility = View.VISIBLE
            binding.llShowDualSim.visibility = View.GONE
        }
    }

    private fun setOnClickListeners() {
        binding.loginText.setOnClickListener {
            val action =
                SignUpFragmentDirections.actionSingUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.clientNumber.doAfterTextChanged {
            if (binding.clientNumber.text.isNullOrEmpty() || binding.clientNumber.text.toString().length != 10) {
                binding.clientNumber.error = "Enter 10-digit Phone Number"
            }
        }
        binding.registerButton.setOnClickListener {
            when {
                binding.clientNumber.text.toString().isEmpty() -> {
                    binding.clientNumber.error = "Field required"
                }
                binding.clientName.text.toString().isEmpty() -> {
                    binding.clientName.error = "Field required"
                }
//                binding.organizationCode.text.toString().isEmpty() -> {
//                    binding.organizationCode.error = "Field required"
//                }
                else -> {
//                    submitDetails()
                }
            }
        }
    }

    private fun checkValidationForOrganizationCode() {
        binding.apply {
            organizationCode.doAfterTextChanged {
                if (it?.length == 17) {
                    //api for validation
                    registrationAndLoginViewModel.getOrganizationDetails("${organizationCode.text}")
                } else {
                    binding.imageValidationCheck.visibility = View.GONE
                    binding.imageValidationFailure.visibility = View.GONE
                }
            }
        }
    }

//    private fun submitDetails() {
//        binding.apply {
//            if (organizationCode.text.toString().trim().isEmpty() && orgCode == null){
//                    val builder = AlertDialog.Builder(requireContext())
//                    builder.setTitle("Continue Without Organization")
//                    builder.setMessage("Are you sure you want to continue without an Organization Code?")
//                    builder.setPositiveButton("Continue") { _, _ ->
//                        registrationAndLoginViewModel.registerNewUser(
//                            binding.ccp.fullNumber, "${clientName.text}", selectedSIM,
//                            subscriptionInfoTelecom,subscriptionInfoSub)
//
//                        builder.create().dismiss()
//                    }
//                    builder.setNegativeButton("No"){_,_ ->
//                        builder.create().dismiss()
//                    }
//                    builder.setCancelable(false)
//                    builder.create()
//                    builder.show()
//            }
//            else if(orgCode == null){
//                val builder = AlertDialog.Builder(requireContext())
//                builder.setTitle("Continue Without Verification?")
//                builder.setMessage("If you continue without the verification, your calls would not be synced to your organization")
//                builder.setPositiveButton("Continue") { _, _ ->
//                    registrationAndLoginViewModel.registerNewUser(
//                        binding.ccp.fullNumber, "${clientName.text}", selectedSIM,
//                        subscriptionInfoTelecom,subscriptionInfoSub)
//                    builder.create().dismiss()
//                }
//                builder.setNegativeButton("No"){_,_ ->
//                    builder.create().dismiss()
//                }
//                builder.setCancelable(false)
//                builder.create()
//                builder.show()
//            } else {
//                registrationAndLoginViewModel.registerNewUser(
//                    binding.ccp.fullNumber, "${clientName.text}", selectedSIM,
//                    subscriptionInfoTelecom,subscriptionInfoSub, orgCode)
//            }
//        }
//    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.apply {
            userRegisteredResponse.observe({ lifecycle }) { response ->
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Success",
                    "User has been registered..",
                    "success", requireContext()
                )
                val intent = Intent(requireContext(), BaseActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            userAlreadyExists.observe({ lifecycle }) { convertedUserData ->
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "User Already Exists",
                    "Redirecting..",
                    "warning", requireContext()
                )
                val intent = Intent(requireContext(), BaseActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }


            simCardInfoTelecom.observe({ lifecycle }) { listOfSubscriptionInfo ->
                simCardCount = listOfSubscriptionInfo.size
                subscriptionInfoTelecom = listOfSubscriptionInfo
                Log.d("simCardInformation", listOfSubscriptionInfo.toString())
                Log.d("simCardInformation2", subscriptionInfoTelecom.toString())
            }

            simCardInfoSub.observe({lifecycle}){
                subscriptionInfoSub = it
            }

            simCardInfo.observe({ lifecycle }) {
                checkSIMCardStatus(it)
            }

            validateOrgCode.observe({ lifecycle }) { organizationCode ->
                orgCode = organizationCode
                binding.imageValidationCheck.visibility = View.VISIBLE
                binding.organizationCode.isEnabled = false
                binding.organizationCode.isClickable = false
            }

            validateOrgCodeFailure.observe({lifecycle}){
                orgCode = null
                binding.imageValidationCheck.visibility = View.GONE
                binding.imageValidationFailure.visibility = View.VISIBLE

            }
            responseFailed.observe({lifecycle}){
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Whoops!",
                    "Something went wrong. Please try again Later",
                    "failure",
                    requireContext())
            }
        }
    }
}