package com.eazybe.callLogger.ui.Onboarding

import android.Manifest
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
import com.eazybe.callLogger.databinding.FragmentPermissionSecondBinding
import com.eazybe.callLogger.ui.SignUpAndLogin.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [PermissionFragmentSecond.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PermissionFragmentSecond : Fragment() {
    private lateinit var binding: FragmentPermissionSecondBinding
        private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val requestReadContactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            Log.d("Permission", "Permission Granted")
            onboardingViewModel.savePermissionAccessStateToPreference()
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            //ask again
            Log.d("Permission", "Permission Rejected")
            onboardingViewModel.clearDataFromPreference()
            activity?.onBackPressed()
//            requestPermissionsToReadContacts()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.agreePermission1Button.setOnClickListener {
//            requestPermissionsToReadContacts()
        }
    }

}