package com.eazybe.callLogger.ui.SignUpAndLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.MainActivity
import com.eazybe.callLogger.databinding.LoginFragmentBinding
import com.eazybe.callLogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.userLastSync.observe({ lifecycle }) { response ->
            GlobalMethods.showMotionToast(
                requireActivity(),
                "Success",
                "Logged in.",
                "success",requireContext()
            )
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // attach mobile number field to country code field
        binding.ccp.registerCarrierNumberEditText(binding.clientNumber)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.registerText.setOnClickListener {
        activity?.onBackPressed()
        }
        binding.clientNumber.doAfterTextChanged {
            if (binding.clientNumber.text.isNullOrEmpty() || binding.clientNumber.text.toString().length != 10) {
                binding.clientNumber.error = "Enter 10-digit Phone Number"
            }
        }
        binding.loginButton.setOnClickListener {
            when {
                binding.clientNumber.text.toString().isEmpty() -> {
                    binding.clientNumber.error = "Field required"
                }
                else -> {
                    submitDetails()
                }
            }
        }
    }

    private fun submitDetails() {
        registrationAndLoginViewModel.checkLastSynced(binding.ccp.fullNumber)
    }
}