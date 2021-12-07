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
import androidx.navigation.fragment.findNavController
import com.eazybe.callLogger.databinding.FragmentPermissionFirstBinding
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.ui.SignUpAndLogin.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [PermissionFragmentFirst.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PermissionFragmentFirst : Fragment() {
    private lateinit var binding: FragmentPermissionFirstBinding
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val requestManageCallsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            Log.d("Permission", "Permission Granted")
            requestReadCallLogsPermission()
        } else {
            //close the app
            onboardingViewModel.clearDataFromPreference()
            activity?.onBackPressed()
            Log.d("Permission", "Permission Rejected")
        }
    }
    private val requestReadCallLogsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                onboardingViewModel.savePermissionAccessStateToPreference()
                val intent = Intent(requireContext(), RegisterActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
//                val action =
//                    PermissionFragmentFirstDirections.actionPermissionFragmentFirstToPermissionFragmentSecond()
//                findNavController().navigate(action)
            } else {
                GlobalMethods.showToast(requireContext(), "Permission Rejected")
//                requestReadCallLogsPermission()
                onboardingViewModel.clearDataFromPreference()
                activity?.onBackPressed()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.agreePermission1Button.setOnClickListener {
            requestPermissionsToManageCalls()
        }
    }

    private fun requestPermissionsToManageCalls() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission Granted")
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE
            )
        } else
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE
            )
    }

    private fun requestReadCallLogsPermission() {
        // You can directly ask for the permission.
        requestReadCallLogsPermission.launch(
            Manifest.permission.READ_CALL_LOG
        )
    }
}