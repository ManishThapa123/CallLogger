package com.eazybe.callLogger.ui.CallLogs.CallLog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.FragmentCallLogPermissionBinding
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogPermissionFragment : Fragment() {
    private lateinit var binding: FragmentCallLogPermissionBinding
    private val callDetailsViewModel: CallLogsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallLogPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            btnSyncCallLogs.setOnClickListener {
                callDetailsViewModel.saveCallLogState()
                findNavController().navigate(R.id.gotoCallLogFragment)
            }
        }
    }
}