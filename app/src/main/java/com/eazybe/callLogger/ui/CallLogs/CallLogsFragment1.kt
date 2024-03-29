package com.eazybe.callLogger.ui.CallLogs

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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.databinding.FragmentCallLogs1Binding
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.allCallLog
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.incomingCallLog
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.missedCallLog
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.rejectedCallLog
import com.eazybe.callLogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogsFragment1 : Fragment() {

    private val integerChars = '0'..'9'
    private lateinit var binding: FragmentCallLogs1Binding
    private lateinit var callDetailsAdapter: CallDetailsAdapter
    private var typeInString: String? = null
    private var userNumberToCall: String? = null
    private val callDetailsViewModel: CallLogsViewModel by activityViewModels()
    private var sectionCallLogs: ArrayList<SampleEntity> = ArrayList()
    private var callDataToDelete: SampleEntity? = null


    private val requestReadCallLogsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                //Get the call logs.
                callDetailsViewModel.getCallLogs(typeInString!!)
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
        //callDetailsViewModel.getAndSaveLatestSyncedTime()
        //call the api and get the time.
        if (!arguments?.getString("type").isNullOrEmpty()) {
            arguments.let {
                typeInString = it?.getString("type")
                //Log.d("Type", typeInString!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //check if the phone_state permission is accepted.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    //Check if the call logs permission is accepted.
            checkCallLogsPermission()
        } else {
            //Ask for the Permission
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE)
        }
    }

    private fun checkCallLogsPermission() {
        //Check if the call logs permission is accepted.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                    //Get the Call Logs
            callDetailsViewModel.getCallLogs(typeInString!!)
        } else {
            //Ask for the Read Call Logs Permission.
            requestReadCallLogsPermission.launch(
                Manifest.permission.READ_CALL_LOG)
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
                userNumberToCall = callData.userNumber
                //Write condition for calling
                GlobalMethods.callUser(callData.userNumber!!, requireContext())

            }

            override fun deleteUser(callData: SampleEntity) {
                callDataToDelete = callData
                //Write condition for deleting
                callDetailsViewModel.deleteUserCallLog(callData, requireContext())
                sectionCallLogs.remove(callData)
                callDetailsAdapter.submitList(sectionCallLogs)
                callDetailsAdapter.notifyDataSetChanged()

            }

            override fun copyContact(callData: SampleEntity) {
                GlobalMethods.copyTextToClipboard(callData.userNumber!!,requireContext())
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
                    sectionCallLogs = sampleData as ArrayList<SampleEntity> ?: return@observe
                    callDetailsAdapter.submitList(sectionCallLogs)
                    callDetailsAdapter.notifyDataSetChanged()
                    callDetailsAdapter.registerAdapterDataObserver(object :
                        RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            super.onItemRangeInserted(positionStart, itemCount)
                            binding.rvCallDetailsFragmentOne.smoothScrollToPosition(0)
                        }

                    })
                    setSearchListener(sectionCallLogs)
                }
                allCallLog.observe({ lifecycle }) {
                    callDetailsViewModel.getCallLogs(typeInString!!)
                }
            }
            2 -> {
                callDetailsViewModel.incomingCallData.observe({ lifecycle }) { sampleData ->
                    sectionCallLogs = sampleData as ArrayList<SampleEntity> ?: return@observe
                    callDetailsAdapter.submitList(sectionCallLogs)
                    Log.d("Type1", typeInString!! + "$sampleData")
                    setSearchListener(sectionCallLogs)
                }
                incomingCallLog.observe({ lifecycle }) {
                    GlobalMethods.showMotionToast(
                        requireActivity(),
                        "Call Synced",
                        "Your calls have been updated",
                        "success", requireContext()
                    )
                    sectionCallLogs.add(0, it)
                    setCallLogsAdapter()
                    callDetailsAdapter.submitList(sectionCallLogs)
                    callDetailsAdapter.notifyItemRangeChanged(0, sectionCallLogs.size)
                    setSearchListener(sectionCallLogs)
                }

            }
            3 -> {
                callDetailsViewModel.outGoingCallData.observe({ lifecycle }) { sampleData ->
                    sectionCallLogs = sampleData as ArrayList<SampleEntity> ?: return@observe
                    callDetailsAdapter.submitList(sectionCallLogs)
                    callDetailsAdapter.notifyDataSetChanged()
                    callDetailsAdapter.registerAdapterDataObserver(object :
                        RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            super.onItemRangeInserted(positionStart, itemCount)
                            binding.rvCallDetailsFragmentOne.smoothScrollToPosition(0)
                        }

                    })
                    setSearchListener(sectionCallLogs)
                }
            }
            4 -> {
                callDetailsViewModel.missedCallData.observe({ lifecycle }) { sampleData ->
                    sectionCallLogs = sampleData as ArrayList<SampleEntity> ?: return@observe
                    callDetailsAdapter.submitList(sectionCallLogs)
                    setSearchListener(sectionCallLogs)
                }
                missedCallLog.observe({ lifecycle }) {
                    GlobalMethods.showMotionToast(
                        requireActivity(),
                        "Call Synced",
                        "Your calls have been updated",
                        "success", requireContext()
                    )

                    sectionCallLogs.add(0, it)
                    setCallLogsAdapter()
                    callDetailsAdapter.submitList(sectionCallLogs)
                    callDetailsAdapter.notifyItemRangeChanged(0, sectionCallLogs.size)
                    setSearchListener(sectionCallLogs)
                }
            }
            5 -> {
                callDetailsViewModel.rejectedCallData.observe({ lifecycle }) { sampleData ->
                    sectionCallLogs = sampleData as ArrayList<SampleEntity> ?: return@observe
                    callDetailsAdapter.submitList(sectionCallLogs)
                    setSearchListener(sectionCallLogs)
                }
                rejectedCallLog.observe({ lifecycle }) {
                    GlobalMethods.showMotionToast(
                        requireActivity(),
                        "Call Synced",
                        "Your calls have been updated",
                        "success", requireContext()
                    )
                    sectionCallLogs.add(0, it)
                    setCallLogsAdapter()
                    callDetailsAdapter.submitList(sectionCallLogs)
                    callDetailsAdapter.notifyItemRangeChanged(0, sectionCallLogs.size)
                    setSearchListener(sectionCallLogs)
                }
            }
        }

    }

    private fun setSearchListener(sectionCallLogs: ArrayList<SampleEntity>) {
        binding.etSearch.doAfterTextChanged {
            if (it?.length!! % 2 == 0) {
                val callList: ArrayList<SampleEntity> = ArrayList()
                sectionCallLogs.forEach { callData ->
                    if (callData.userName.isNullOrEmpty()) {
                        callData.userName = "Unknown"
                    }
                    callList.add(callData)
                }
                setCallLogsAdapter()

                val isNumber = isInteger(it.toString())
                if (isNumber) {
                    callDetailsAdapter.submitList(
                        callList.filter { profile ->
                            profile.userNumber?.lowercase()?.contains(it.toString())!!
                        }
                    )
                } else {
                    callDetailsAdapter.submitList(
                        callList.filter { profile ->
                            profile.userName?.lowercase()?.contains(it.toString())!!
                        }
                    )
                }
            } else if (it.length == 0) {
                setCallLogsAdapter()
                callDetailsAdapter.submitList(sectionCallLogs)
            }
        }

    }

    private fun isInteger(input: String) = input.all { it in integerChars }
}