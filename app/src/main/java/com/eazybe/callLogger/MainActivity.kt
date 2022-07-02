package com.eazybe.callLogger

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.eazybe.callLogger.databinding.ActivityMainBinding
import com.eazybe.callLogger.databinding.NavigationHeaderBinding
import com.eazybe.callLogger.extensions.toast
import com.eazybe.callLogger.helper.CallLogsUpdatingManager.allCallLog
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.interfaces.ScreenshotInterface
import com.eazybe.callLogger.keyboard.view.MyKeyboardView
import com.eazybe.callLogger.ui.AddOrganization.AddOrganizationActivity
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment1
import com.eazybe.callLogger.ui.CallLogs.CallLogsFragment2
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewPagerAdapter
import com.eazybe.callLogger.ui.Dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ScreenshotInterface {
    private lateinit var binding: ActivityMainBinding
    private var context: Context? = null
    private val callDetailsViewModel: CallLogsViewModel by viewModels()

    private var keyboardView: MyKeyboardView? = null
    private var mgr: MediaProjectionManager? = null
    private var mediaProjection : MediaProjection? = null
    private lateinit var headerBinding: NavigationHeaderBinding
    private val requestReadMediaProjectionPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mediaProjection = mgr!!.getMediaProjection(result.resultCode, result.data!!)

                toast("Accepted permission")
            } else {
                toast("Denied permission")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val headerView: View = binding.navView.getHeaderView(0)
        headerBinding = NavigationHeaderBinding.bind(headerView)
        setContentView(view)

        keyboardView = MyKeyboardView(this,null)
        keyboardView!!.setListener(this)
        //For Navigation Drawer and Tool bar
        setSupportActionBar(binding.contentMain.toolBar)
        title = "Home"
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
//        callDetailsViewModel.permissionState.observe({ lifecycle }) {
//            if (!it) {
//                Log.d("autoSavePermissionState","False")
//                GlobalMethods.checkIfAutoStartPermissionAvailable(this)
//                GlobalMethods.getAutoStartPermission(this)
//                callDetailsViewModel.saveAutoRunPermissionSavedState()
//            }
//        }
//        askForMediaProjectionPermission()
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
                R.id.leadsFrag -> {
                    val intent = Intent(this, BaseActivity::class.java)
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

//        val adapter = CallLogsViewPagerAdapter(fragsList, this)
//        binding.contentMain.fragmentViewPager.offscreenPageLimit = 7
//        binding.contentMain.fragmentViewPager.adapter = adapter

//        TabLayoutMediator(
//            binding.contentMain.tabLayout, binding.contentMain.fragmentViewPager
//        ) { tab, position ->
//            binding.apply {
//                when (position) {
//                    0 -> {
//                        tab.text = "All"
//                        tab.icon =
//                            AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_all_call)
//                    }
//                    1 -> {
//                        tab.text = "Incoming"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_incoming
//                        )
//                    }
//                    2 -> {
//                        tab.text = "Outgoing"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_outgoing
//                        )
//                    }
//                    3 -> {
//                        tab.text = "Missed"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_missed
//                        )
//                    }
//                    4 -> {
//                        tab.text = "Rejected"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_rejected
//                        )
//                    }
//                    5 -> {
//                        tab.text = "Never Attended"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_never_attented
//                        )
//                    }
//                    6 -> {
//                        tab.text = "Not Picked up by client"
//                        tab.icon = AppCompatResources.getDrawable(
//                            this@MainActivity,
//                            R.drawable.ic_not_picked_up_by_client
//                        )
//                    }
//                }
//            }
//        }.attach()

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
    private fun askForMediaProjectionPermission(){
        mgr = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        requestReadMediaProjectionPermission.launch(mgr!!.createScreenCaptureIntent())
    }

    override fun getScreenshotPermissions() {
    toast("Came here ask for permissions")
    }
}
