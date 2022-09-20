package com.eazybe.callLogger.ui.CallLogs.CallLog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.FragmentCallLogPermissionBinding
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.eazybe.callLogger.ui.CallLogs.PermissionDialogFragment
import com.eazybe.callLogger.ui.CallLogs.SimCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.imageradiobutton.GravityImageRadioButton
import xyz.teamgravity.imageradiobutton.GravityRadioGroup

@AndroidEntryPoint
class CallLogPermissionFragment : Fragment() {
    private lateinit var binding: FragmentCallLogPermissionBinding
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private val simCardViewModel: SimCardViewModel by viewModels()

    private var simCardOneName: String? = ""
    private var simCardTwoName: String? = ""
    private var checkedIDDefault: String? = ""
    private var section: String? = null
    private var simCardCount: Int? = 0
    private var subscriptionInfoTelecom: ArrayList<String> = ArrayList()
    private var subscriptionInfoSub: ArrayList<String> = ArrayList()
    private var selectedSIM: Int = 1

    private val requestReadCallLogsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                //Get the call logs.
                simCardViewModel.getSimCardInfos(requireActivity())
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

    private fun checkCallLogsPermission() {
        //Check if the call logs permission is accepted.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            simCardViewModel.getSimCardInfos(requireActivity())
            observeViewModel()
        } else {
            //Ask for the Read Call Logs Permission.
            requestReadCallLogsPermission.launch(
                Manifest.permission.READ_CALL_LOG
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                when (selectedSIM) {
                    2 -> {

                        selectedSIM = 1

                    }
                    else -> {
                        activity?.onBackPressed()
                    }
                }

            }
        })
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

        arguments.let {
            section = it?.getString("tabName").toString()
        }
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
        if (section == "report") {
            binding.btnSyncCallLogs.visibility = View.GONE
            binding.selectSimText.visibility = View.VISIBLE

        }
        setOnClickListener()
    }

    private fun checkSIMCardStatus(simCardInfo: ArrayList<SubscriptionInfo>) {
        if (simCardCount == 1) {
            selectedSIM = 1
            simCardOneName = "${simCardInfo[0].carrierName}"
            Log.d("Sim Card", simCardCount.toString())

            simVisibleCount(1)

        } else if (simCardCount == 2) {
            simCardOneName = "${simCardInfo[0].carrierName}"
            simCardTwoName = "${simCardInfo[1].carrierName}"
            Log.d("Sim Card", simCardCount.toString())
            simVisibleCount(2)
            checkedIDDefault = "${binding.doubleSimRadio.checkedRadioButtonId()}"
        }


        binding.doubleSimRadio.setOnCheckedChangeListener { radioGroup, radioButton, checked, checkedId ->
            Log.d("ID is", "$checkedId, $checked, ${(radioButton as GravityImageRadioButton).text()}, $checkedIDDefault")
            selectedSIM = if ("$checkedId" == "$checkedIDDefault"){
                1
            }else{
                2
            }
        }

        binding.singleSimRadio.setOnCheckedChangeListener { radioGroup, radioButton, checked, checkedId ->
            Log.d("ID is", "$checkedId, $checked, ${(radioButton as GravityImageRadioButton).text()}")
            if ((radioButton).text() == simCardOneName) {
                selectedSIM = 1
            }
        }
    }

    private fun simVisibleCount(simCount: Int) {

        when (simCount) {
            1 -> {
                binding.doubleSimRadio.visibility = View.INVISIBLE
                binding.lLSimText.visibility = View.INVISIBLE
                binding.singleSimRadio.visibility = View.VISIBLE
                binding.sim1Single.setText("$simCardOneName")
                binding.sim1Single.isChecked = true
            }
            2 -> {
                binding.singleSimRadio.visibility = View.INVISIBLE
                binding.doubleSimRadio.visibility = View.VISIBLE
                binding.lLSimText.visibility = View.VISIBLE
                binding.sim1.setText("$simCardOneName")
                binding.sim2.setText("$simCardTwoName")
                binding.sim1.isChecked = true
            }
        }

    }

    private fun setOnClickListener() {
        binding.apply {
            btnSyncCallLogs.setOnClickListener {
                callDetailsViewModel.saveCallLogState()
                simCardViewModel.saveSimDataInPref(selectedSIM, subscriptionInfoTelecom, subscriptionInfoSub)
            }
        }
    }

    private fun observeViewModel() {

        simCardViewModel.apply {

            goToCallLogsFragment.observe({ lifecycle }) {
                if (it) {
                    //first call dilogue.
//                    val dialog = PermissionDialogFragment()
//                    dialog.show(parentFragmentManager, "permission_fragment")
                    findNavController().navigate(R.id.gotoCallLogFragment)
//                    val dialog = PermissionDialogFragment()
//                    dialog.show(parentFragmentManager, "permission_fragment")
                }
            }

            simCardInfoTelecom.observe({ lifecycle }) { listOfSubscriptionInfo ->
                simCardCount = listOfSubscriptionInfo.size
                subscriptionInfoTelecom = listOfSubscriptionInfo
                Log.d("simCardInformation", listOfSubscriptionInfo.toString())
                Log.d("simCardInformation2", subscriptionInfoTelecom.toString())
            }

            simCardInfoSub.observe({ lifecycle }) {
                subscriptionInfoSub = it
            }

            simCardInfo.observe({ lifecycle }) {
                checkSIMCardStatus(it)
            }
        }
    }
}