package com.twango.calllogger.ui.SignUpAndLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // attach mobile number field to country code field
        binding.ccp.registerCarrierNumberEditText(binding.clientNumber)
        setOnClickListeners()

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