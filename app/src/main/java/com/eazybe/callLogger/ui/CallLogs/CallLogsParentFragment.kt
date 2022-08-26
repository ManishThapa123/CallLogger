package com.eazybe.callLogger.ui.CallLogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.FragmentCallLogsParentBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_call_logs_parent.*

@AndroidEntryPoint
class CallLogsParentFragment : Fragment() {
    private lateinit var binding: FragmentCallLogsParentBinding
    private lateinit var navController: NavController
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private var accessState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun observeViewModel() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentCallLogsParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accessState = callDetailsViewModel.getCallLogAccessState()
        //Finding the Navigation Controller
        navController =
            binding.root.let { Navigation.findNavController(it.findViewById(R.id.activitiesFragmentNavHost)) }
        if (accessState)
            navController.navigate(R.id.gotoCallLogFragment)
        else {
            navController.navigate(R.id.gotoPermissionsFragment)
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {
                        val accessState2 = callDetailsViewModel.getCallLogAccessState()
                        if (accessState2)
                            navController.navigate(R.id.gotoCallLogFragment)
                        else {
                            navController.navigate(R.id.gotoPermissionsFragment, bundleOf("tabName" to "call"))
                        }

                    }
                    1 -> {
                        val accessState2 = callDetailsViewModel.getCallLogAccessState()
                        if (accessState2)
                            navController.navigate(R.id.gotoReportsFragment)
                        else

                            navController.navigate(R.id.gotoPermissionsFragment, bundleOf("tabName" to "report"))

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }


}