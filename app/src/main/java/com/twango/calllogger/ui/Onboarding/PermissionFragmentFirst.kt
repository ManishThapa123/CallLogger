package com.twango.calllogger.ui.Onboarding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.twango.calllogger.databinding.FragmentPermissionFirstBinding
import com.twango.calllogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [PermissionFragmentFirst.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PermissionFragmentFirst : Fragment() {
    private lateinit var binding: FragmentPermissionFirstBinding

    private val requestManageCallsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            Log.d("Permission", "Permission Granted")
            requestReadCallLogsPermission()
        } else {
            //ask again
           activity?.onBackPressed()
            Log.d("Permission", "Permission Rejected")
        }
    }
    private val requestReadCallLogsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
//                GlobalMethods.showToast(requireContext(), "Permission Granted")
                val action =
                    PermissionFragmentFirstDirections.actionPermissionFragmentFirstToPermissionFragmentSecond()
                findNavController().navigate(action)
            } else {
                GlobalMethods.showToast(requireContext(), "Permission Rejected")
                requestReadCallLogsPermission()
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
        } else
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE
            )
    }

    private fun requestReadCallLogsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission Granted")

        } else
        // You can directly ask for the permission.
            requestReadCallLogsPermission.launch(
                Manifest.permission.READ_CALL_LOG
            )
    }
}