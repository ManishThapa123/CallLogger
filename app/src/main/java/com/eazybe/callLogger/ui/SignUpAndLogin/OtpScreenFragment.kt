package com.eazybe.callLogger.ui.SignUpAndLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.BaseActivity
import com.eazybe.callLogger.databinding.FragmentOtpScreenBinding
import com.eazybe.callLogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpScreenFragment : Fragment() {

    private lateinit var binding: FragmentOtpScreenBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()

    private var userEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!arguments?.getString("email").isNullOrEmpty()) {
            arguments.let {
                userEmail = it?.getString("email")
            }
        }
        observeViewModel()
        setOnClickListeners()
    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.resendOTP.observe({ lifecycle }) {
            if (it) {
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Success!",
                    "OTP has been sent to your email.",
                    "success",
                    requireContext()
                )
            } else {
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Whoops!",
                    "OTP could not be sent.",
                    "failure",
                    requireContext()
                )
            }
        }

        registrationAndLoginViewModel.emailVerified.observe({ lifecycle }) {

            //Hit the Create user api and save the user id and his registration date to the preference,
            //Then navigate the use to the Base activity.
            if (it.status == true) {

                registrationAndLoginViewModel.createOrUpdateCallyzerUser()

            } else {
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Whoops!",
                    "Incorrect OTP.",
                    "failure",
                    requireContext()
                )
            }
        }

        registrationAndLoginViewModel.userRegisteredResponse.observe({lifecycle}){
            val intent = Intent(requireContext(), BaseActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            enterOtpEmailTxt.text = userEmail
            pinView.setPinViewEventListener { pinview, fromUser ->
                GlobalMethods.hideKeyboard(requireContext(), binding.root)
            }
            ibBack.setOnClickListener {
                activity?.onBackPressed()
            }
            resendOtp.setOnClickListener {
                registrationAndLoginViewModel.resendOtp(userEmail!!)
            }
            signUpBtn.setOnClickListener {
                registrationAndLoginViewModel.verifyEmailOtp(userEmail!!, pinView.value.toInt())
            }
        }
    }

}