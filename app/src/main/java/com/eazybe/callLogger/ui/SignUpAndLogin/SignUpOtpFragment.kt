package com.eazybe.callLogger.ui.SignUpAndLogin

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amplifyframework.core.Amplify
import com.eazybe.callLogger.BaseActivity
import com.eazybe.callLogger.databinding.FragmentSignUpOtpBinding
import com.eazybe.callLogger.helper.GlobalMethods
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Suppress("DEPRECATION")
class SignUpOtpFragment : Fragment() {

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1000
    private lateinit var binding: FragmentSignUpOtpBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Check if the call logs permission is accepted.
            checkCallLogsPermission()
        } else {
            //Ask for the Permission
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE
            )
        }


    }

    private fun checkCallLogsPermission() {
        //Check if the call logs permission is accepted.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createGoogleSignInOptions()
            observeViewModel()
        } else {
            //Ask for the Read Call Logs Permission.
            requestReadCallLogsPermission.launch(
                Manifest.permission.READ_CALL_LOG
            )
        }
    }

    private fun createGoogleSignInOptions() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        gsc = GoogleSignIn.getClient(requireActivity(), gso!!);
    }

    private fun observeViewModel() {
        registrationAndLoginViewModel.sendToOtpScreen.observe({ lifecycle }) {
            //Navigate to OTP Screen

            val action =
                SignUpOtpFragmentDirections.actionSignUpOtpFragmentToOtpScreenFragment(it)
            findNavController().navigate(action)
        }

        /**
         * To get the auth from the gmail SDK.
         */
        registrationAndLoginViewModel.gmailVerified.observe({ lifecycle }) {

            //Hit the Create user api and save the user id and his registration date to the preference,
            //Then navigate the use to the Base activity.
            if (it) {
                registrationAndLoginViewModel.createOrUpdateCallyzerUser()
            } else {
                GlobalMethods.showMotionToast(
                    requireActivity(),
                    "Whoops!",
                    "Failed to verify you email. Please try again.",
                    "failure",
                    requireContext()
                )
            }
        }

        registrationAndLoginViewModel.configureAmplify.observe({lifecycle}){convertedUserData ->
            if (convertedUserData != null){
                Amplify.DataStore.start(
                    { Log.i("MyAmplifyApp", "DataStore started")
                        registrationAndLoginViewModel.createAmplifyUser(convertedUserData)},
                    { Log.e("MyAmplifyApp", "Error starting DataStore", it) }
                )


            }

        }

        registrationAndLoginViewModel.userRegisteredResponse.observe({ lifecycle }) {
            val intent = Intent(requireContext(), BaseActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnVerify.setOnClickListener {
                when {
                    txtEmail.text.toString().isEmpty() -> {
                        txtEmail.error = "Email required"
                    }
                    else -> {
                        verifyEmail()
                    }
                }
            }

            googleSignInButton.setOnClickListener {
                val signInIntent: Intent = gsc!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    private fun verifyEmail() {
        binding.apply {
            registrationAndLoginViewModel.verifyEmailAndSendOtp("${txtEmail.text}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                // Signed in successfully, call API to verify and redirect to the base activity.
                val email = account.email
                registrationAndLoginViewModel.verifyGmailSignUp(email!!, "000000")

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }
}