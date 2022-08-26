package com.eazybe.callLogger.ui.CallLogs.CallLog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.FragmentCallLogMainBinding
import com.eazybe.callLogger.interfaces.UpdateExpiryTime
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment1
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment2
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogMainFragment : Fragment() {
    private lateinit var binding: FragmentCallLogMainBinding
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private var updateExpiryTime: UpdateExpiryTime? = null

//    As soon as user comes for first time to Calls Screen show him the Sim selection
//
//After selecting the sim show Call Logs and on top where Sync button is there, show a button that asks the user to sync call logs
//
//After he clicks on that ask him if he wants to sync call logs to server
//
//After this process is the same
//
//Reports section for a user who has not synced his call logs will also be different as there you'll ask the user to sync call logs and redirect to same popup that asks him to sync call logs
//
//App me whatsapp screen Sync Whatsapp UI still left

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callDetailsViewModel.saveRegisteredDateAndTime()
//        callDetailsViewModel.getSyncedTimeAndSaveInPreference()
        observeViewModel()
    }

    private fun observeViewModel() {
        callDetailsViewModel.updateExpiryTime.observe({lifecycle}){
            updateExpiryTime?.updateExpiryTime(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UpdateExpiryTime)
            updateExpiryTime = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentCallLogMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        //Call it here phonebook.
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                hideProgressBar()
                setUpAdapter()
            }, 600)
        } catch (e: Exception) {

        }
        setOnClickListener()
    }

    private fun showProgressBar() {
        binding.apply {
            tabLayout.visibility = View.GONE
            fragmentViewPager.visibility = View.GONE
            pbProgress.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            tabLayout.visibility = View.VISIBLE
            fragmentViewPager.visibility = View.VISIBLE
            pbProgress.visibility = View.GONE
        }
    }

    private fun setOnClickListener() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:")
            startActivity(intent)
        }
    }

    private fun setUpAdapter() {
        val fragsList: ArrayList<Fragment> = ArrayList()
        for (i in 1..7) {
            when (i) {
                6, 7 -> {
                    val fragment = CallLogsFragment2.newInstance("$i")
                    fragsList.add(fragment)
                }
                else -> {
                    val fragment = CallLogsFragment1.newInstance("$i")
                    fragsList.add(fragment)
                }
            }
        }

        val adapter = CallLogsViewPagerAdapter(fragsList, this)
        binding.fragmentViewPager.offscreenPageLimit = 7
        binding.fragmentViewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabLayout, binding.fragmentViewPager
        ) { tab, position ->
            binding.apply {
                when (position) {
                    0 -> {
                        tab.text = "All"
                        tab.icon =
                            AppCompatResources.getDrawable(
                                requireActivity(),
                                R.drawable.ic_all_call
                            )
                    }
                    1 -> {
                        tab.text = "Incoming"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_incoming
                        )
                    }
                    2 -> {
                        tab.text = "Outgoing"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_outgoing
                        )
                    }
                    3 -> {
                        tab.text = "Missed"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_missed
                        )
                    }
                    4 -> {
                        tab.text = "Rejected"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_rejected
                        )
                    }
                    5 -> {
                        tab.text = "Never Attended"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_never_attented
                        )
                    }
                    6 -> {
                        tab.text = "Not Picked up by client"
                        tab.icon = AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_not_picked_up_by_client
                        )
                    }
                }
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    5 -> {
                        binding.apply {
                            floatingActionButton.hide()
                            Handler(Looper.getMainLooper()).postDelayed({

                                floatingActionButton.show()
                            }, 600)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("unselectedTab", "${tab?.text}")

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}