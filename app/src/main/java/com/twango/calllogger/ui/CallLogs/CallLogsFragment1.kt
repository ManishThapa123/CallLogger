package com.twango.calllogger.ui.CallLogs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.databinding.FragmentCallLogs1Binding
import com.twango.calllogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogsFragment1 : Fragment() {

    private lateinit var binding: FragmentCallLogs1Binding
    private lateinit var callDetailsAdapter: CallDetailsAdapter
    private var typeInString: String? = null
    private val callDetailsViewModel: CallLogsViewModel by activityViewModels()

    //To request the call phone permission.
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                GlobalMethods.showToast(requireContext(), "Permission Granted")
            } else GlobalMethods.showToast(requireContext(), "Permission Rejected")
        }

    companion object {
        fun newInstance(type: String): Fragment {
            val bundle = bundleOf("type" to type)
            val fragment = CallLogsFragment1()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callDetailsViewModel.getAndSaveLatestSyncedTime()
        if (!arguments?.getString("type").isNullOrEmpty()) {
            arguments.let {
                typeInString = it?.getString("type")
                callDetailsViewModel.getCallLogs(typeInString!!)
//                Log.d("Type", typeInString!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCallLogs1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setCallLogsAdapter()

    }

    //To attach the adapter
    private fun setCallLogsAdapter() {
        Log.d("adapterType", typeInString!!)
        callDetailsAdapter = object : CallDetailsAdapter() {
            override fun getContext(): Context {
                return requireContext()
            }

            /**
             * To send a text message to the user when the message icon is clicked.
             */
            override fun sendMessageToUser(callData: SampleEntity) {
                GlobalMethods.sendSmsToUser(callData.userNumber!!, requireContext())
            }

            /**
             * To open the number is whats app.
             */
            override fun sendWhatsAppToUser(callData: SampleEntity) {
                GlobalMethods.openNumberInWhatsapp(callData.userNumber!!, requireContext())
            }

            /**
             * Checks whether the permission CALL_PHONE is granted or not.
             * If the permission is not granted, the [requestPermission] is launched, to request the permission.
             * When granted the call is made to the user number.
             */
            override fun callUser(callData: SampleEntity) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("Permission", "Permission Granted")
                    //Write condition for calling
                    GlobalMethods.callUser(callData.userNumber!!, requireContext())
                } else
                // You can directly ask for the permission.
                    requestPermission.launch(
                        Manifest.permission.CALL_PHONE
                    )
            }
        }
        binding.rvCallDetailsFragmentOne.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = callDetailsAdapter
        }
    }

    /**
     * In order to observe the Livedata.
     */
    private fun observeViewModel() {
        Log.d("Type", typeInString!!)
        when (typeInString?.toInt()) {
            1 -> {
                callDetailsViewModel.sampleData.observe({ lifecycle }) { sampleData ->
                    callDetailsAdapter.submitList(sampleData)
                }

            }
            2 -> {
                callDetailsViewModel.incomingCallData.observe({ lifecycle }) { sampleData ->
                    callDetailsAdapter.submitList(sampleData)
                    Log.d("Type1", typeInString!! + "$sampleData")
                }

            }
            3 -> {
                callDetailsViewModel.outGoingCallData.observe({ lifecycle }) { sampleData ->
                    callDetailsAdapter.submitList(sampleData)
                }
            }
            4 -> {
                callDetailsViewModel.missedCallData.observe({ lifecycle }) { sampleData ->
                    callDetailsAdapter.submitList(sampleData)
                }
            }
            5 -> {
                callDetailsViewModel.rejectedCallData.observe({ lifecycle }) { sampleData ->
                    callDetailsAdapter.submitList(sampleData)
                }
            }
        }
    }

}