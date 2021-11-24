package com.twango.calllogger.ui.SignUpAndLogin

import android.content.Intent
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.twango.calllogger.MainActivity
import com.twango.calllogger.databinding.SingUpFragmentBinding
import com.twango.calllogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: SingUpFragmentBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()
    private var simCardOneName: String? = ""
    private var simCardTwoName: String? = ""
    private var simCardCount: Int? = 0
    private var simCardInfo: ArrayList<SubscriptionInfo> = ArrayList()
    private var selectedSIM: Int? = null

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
                        binding.simTwoCarrier.visibility = View.GONE
                        selectedSIM = 1
                        binding.llShowDualSim.visibility = View.VISIBLE
                    }
                    else -> {
                        activity?.onBackPressed()
                    }
                }

            }
        })
        simCardInfo =
            GlobalMethods.getSimCardInfos(requireActivity()) as ArrayList<SubscriptionInfo>
        simCardCount = simCardInfo.size
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // attach mobile number field to country code field
        binding.ccp.registerCarrierNumberEditText(binding.clientNumber)
        setOnClickListeners()
        checkSIMCardStatus()
    }

    private fun checkSIMCardStatus() {
        if (simCardCount == 1) {
            simCardOneName = "${simCardInfo[0].carrierName}"
            Log.d("Sim Card", simCardCount.toString())
            binding.simOneCarrier.text = simCardOneName
            selectedSIM = 1
        } else {
            binding.llShowDualSim.visibility = View.VISIBLE
            simCardOneName = "${simCardInfo[0].carrierName}"
            simCardTwoName = "${simCardInfo[1].carrierName}"
            binding.simOneCarrier.text = simCardOneName
            binding.simTwoCarrier.text = simCardTwoName
            Log.d("Sim Card", simCardCount.toString())
        }

        binding.btnSkipSimOne.setOnClickListener {
            binding.simOneCarrier.visibility = View.GONE
            binding.simTwoCarrier.visibility = View.VISIBLE
            selectedSIM = 2
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
                else -> {
                    submitDetails()
                }
            }
        }
    }

    private fun submitDetails() {
        binding.apply {
            registrationAndLoginViewModel.registerNewUser(
                binding.ccp.fullNumberWithPlus,
                "${clientName.text}"
            )
        }
    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.apply {
            userRegisteredResponse.observe({ lifecycle }) { response ->
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Success",
                    "User has been registered..",
                    "success", requireContext()
                )
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            userAlreadyExists.observe({ lifecycle }) { response ->
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "User Already Exists",
                    "Redirecting..",
                    "info", requireContext()
                )
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

    }

}