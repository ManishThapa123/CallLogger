package com.twango.calllogger

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.twango.calllogger.databinding.ActivityMainBinding
import com.twango.calllogger.helper.GlobalMethods
import com.twango.calllogger.ui.CallLogs.CallLogsFragment1
import com.twango.calllogger.ui.CallLogs.CallLogsFragment2
import com.twango.calllogger.ui.CallLogs.CallLogsViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var context: Context? = null

    private val requestManageCallsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            Log.d("Permission", "Permission Granted")
            requestPermissionsToReadContacts()
        } else {
            //ask again
            requestPermissionsToManageCalls()
            Log.d("Permission", "Permission Rejected")
        }

    }
    private val requestReadContactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted: Boolean ->
        if (permissionGranted) {
            Log.d("Permission", "Permission Granted")
            GlobalMethods.checkIfAutoStartPermissionAvailable(this)
            GlobalMethods.getAutoStartPermission(this)
        } else {
            //ask again
            Log.d("Permission", "Permission Rejected")
            requestPermissionsToReadContacts()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
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

        //To request Permission
        requestPermissionsToManageCalls()
        setUpAdapter()
        binding.contentMain.floatingActionButton.setOnClickListener {
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
                            R.drawable.call_not_interested
                        )
                    }
                    5 -> {
                        tab.text = "Never Attended"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_not_received
                        )
                    }
                    6 -> {
                        tab.text = "Not Picked up by client"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_disabled
                        )
                    }
                }
            }
        }.attach()

        binding.contentMain.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Never Attended" -> {
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

    private fun requestPermissionsToReadContacts() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission Granted")
        } else
            requestReadContactsPermission.launch(
                Manifest.permission.READ_CONTACTS
            )
    }

    private fun requestPermissionsToManageCalls() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Permission Granted")
        } else
            requestManageCallsPermission.launch(
                Manifest.permission.READ_PHONE_STATE
            )
    }

}
