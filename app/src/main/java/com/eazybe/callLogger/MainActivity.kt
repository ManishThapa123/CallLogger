package com.eazybe.callLogger

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.eazybe.callLogger.databinding.ActivityMainBinding
import com.eazybe.callLogger.databinding.NavigationHeaderBinding
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.allCallLog
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.ui.AddOrganization.AddOrganizationActivity
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment1
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment2
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewPagerAdapter
import com.eazybe.callLogger.ui.Dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var context: Context? = null
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private lateinit var headerBinding: NavigationHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val headerView: View = binding.navView.getHeaderView(0)
        headerBinding = NavigationHeaderBinding.bind(headerView)
        setContentView(view)

        //For Navigation Drawer and Tool bar
        setSupportActionBar(binding.contentMain.toolBar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.contentMain.toolBar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        //Call it here phonebook.
        setUpAdapter()

        callDetailsViewModel.getAutoRunPermissionSavedState()
        callDetailsViewModel.permissionState.observe({ lifecycle }) {
            if (!it) {
                Log.d("autoSavePermissionState","False")
                GlobalMethods.checkIfAutoStartPermissionAvailable(this)
                GlobalMethods.getAutoStartPermission(this)
                callDetailsViewModel.saveAutoRunPermissionSavedState()
            }
        }
        // call the view model to save the registered date in millis.
        callDetailsViewModel.saveRegisteredDateAndTime()

        allCallLog.observe({ lifecycle }) {
            callDetailsViewModel.getLastSynced()
        }
        callDetailsViewModel.lastSynced.observe({ lifecycle }) {
            if (!it.isNullOrEmpty()) {
                headerBinding.lastSynced.text =
                    "Last Synced: ${GlobalMethods.convertMillisToDateAndTimeInMinutes(it)}"
            } else
                headerBinding.lastSynced.text = ""
        }
        binding.contentMain.floatingActionButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:")
            startActivity(intent)
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navSettings -> {
//                    val intent = Intent(this, RegisterActivity::class.java)
//                    intent.flags =
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)

                }
                R.id.navAbout -> {
//                   val intent = Intent(this, OnBoardingActivity::class.java)
//                   intent.flags =
//                       Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                   startActivity(intent)
                }
                R.id.dashBoard -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.close()
                }

                R.id.navOrg -> {
                    val intent = Intent(this, AddOrganizationActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.close()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        callDetailsViewModel.getLastSynced()
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
        binding.contentMain.fragmentViewPager.offscreenPageLimit = 7
        binding.contentMain.fragmentViewPager.adapter = adapter

        TabLayoutMediator(
            binding.contentMain.tabLayout, binding.contentMain.fragmentViewPager
        ) { tab, position ->
            binding.apply {
                when (position) {
                    0 -> {
                        tab.text = "All"
                        tab.icon =
                            AppCompatResources.getDrawable(this@MainActivity, R.drawable.call_icon)
                    }
                    1 -> {
                        tab.text = "Incoming"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_received_icon
                        )
                    }
                    2 -> {
                        tab.text = "Outgoing"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_outgoing_icon
                        )
                    }
                    3 -> {
                        tab.text = "Missed"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_missed_icon
                        )
                    }
                    4 -> {
                        tab.text = "Rejected"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_disabled
                        )
                    }
                    5 -> {
                        tab.text = "Never Attended"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_never_attented
                        )
                    }
                    6 -> {
                        tab.text = "Not Picked up by client"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_never_picked_up
                        )
                    }
                }
            }
        }.attach()

        binding.contentMain.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    5 -> {
                        binding.apply {
                            contentMain.floatingActionButton.hide()
                            Handler(Looper.getMainLooper()).postDelayed({
                                contentMain.floatingActionButton.show()
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
