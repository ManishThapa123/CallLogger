package com.eazybe.callLogger.ui.SignUpAndLogin.Onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.BaseActivity
import com.eazybe.callLogger.databinding.FragmentOnboardingOrganizationBinding
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.ui.SignUpAndLogin.RegistrationAndLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragmentOrganization : Fragment() {
    private lateinit var binding: FragmentOnboardingOrganizationBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.apply {
            validateOrgCode.observe({ lifecycle }) { organizationCode ->
                addOrganization(organizationCode)
            }

            userAlreadyHasOrgCode.observe({ lifecycle }) {
                if (it) {
                    GlobalMethods.showMotionToast(
                        requireActivity(),
                        "Whoops!",
                        "Looks like your number is already synced to an Organization.",
                        "info",
                        requireContext()
                    )
                    val intent = Intent(requireContext(), BaseActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
            orgCodeResponse.observe({ lifecycle }) {
                if (it) {
                    GlobalMethods.showMotionToast(
                        requireActivity(),
                        "Success!",
                        "Organization Added.",
                        "success",
                        requireContext()
                    )
                    val intent = Intent(requireContext(), BaseActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingOrganizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            if (binding.txtOrg.length() > 16) {
                registrationAndLoginViewModel.getOrganizationDetails("${binding.txtOrg.text}")
            } else {
                GlobalMethods.showToast(requireContext(), "Invalid Organization Code")
            }
        }
        binding.txtSkip.setOnClickListener {
            val intent = Intent(requireContext(), BaseActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }


}