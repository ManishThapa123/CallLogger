package com.eazybe.callLogger

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.eazybe.callLogger.databinding.ActivityBaseBinding
import com.eazybe.callLogger.extensions.toast
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.interfaces.ScreenshotInterface
import com.eazybe.callLogger.interfaces.UpdateExpiryTime
import com.eazybe.callLogger.keyboard.view.MyKeyboardView
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.eazybe.callLogger.ui.CallLogs.PermissionDialogFragment
import com.eazybe.callLogger.ui.Plans.PlansFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * New Architecture along with the bottom navigation
 */
@AndroidEntryPoint
class BaseActivity : AppCompatActivity(), ScreenshotInterface, UpdateExpiryTime {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var navController: NavController
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private var mgr: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var keyboardView: MyKeyboardView? = null
    private var animation: Animation? = null

    private val requestReadContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted: Boolean ->
            if (permissionGranted) {
                //Get the call logs.

            } else {
                //close the app
            }
        }
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
        binding = ActivityBaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        keyboardView = MyKeyboardView(this, null)
//        keyboardView!!.setListener(this)

        callDetailsViewModel.getSyncedTimeAndSaveInPreference()
        observeViewModel()
        //Finding the Navigation Controller
        val navController = findNavController(R.id.myNavHostFragment)

        //Setting Navigation Controller with the BottomNavigationView
        binding.bottomNavigation.setupWithNavController(navController)

        setupBottomNavigation()
        checkCallLogsPermission()
        setOnClickListener()


//        askForMediaProjectionPermission()
    }

    private fun animateIcon() {
        animation = AnimationUtils.loadAnimation(this,R.anim.rotate)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.syncIcon.startAnimation(animation)
        },1500)

    }

    private fun setOnClickListener() {
        binding.lLTimeLeftText.setOnClickListener {
            syncCallLogsAndGetExpiryTime()
        }
    }

    private fun observeViewModel() {

        callDetailsViewModel.getSyncDateFromAPI.observe({ lifecycle }) {
            if (it) {
                callDetailsViewModel.getSyncedTimeAndSaveInPreference(true)
            }
        }


        callDetailsViewModel.lastSyncedTime.observe({ lifecycle }) { syncTime ->
            binding.apply {
                lLTimeLeftText.visibility = View.VISIBLE

                when (syncTime) {
                    "Not_Synced" -> {
                        timeLeftTxt.text = "Sync Call Logs"
                        lLTimeLeftText.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@BaseActivity, R.color.grey_light
                                )
                            )
                        timeLeftTxt.setTextColor(
                            ContextCompat.getColor(
                                this@BaseActivity, R.color.green_light
                            )
                        )
                        animateIcon()
                    }
                    "Syncing" -> {
                        binding.timeLeftTxt.text = "Syncing"
                        lLTimeLeftText.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@BaseActivity, R.color.green_lighter
                                )
                            )
                        timeLeftTxt.setTextColor(
                            ContextCompat.getColor(
                                this@BaseActivity, R.color.green_light
                            )
                        )
                        animateIcon()
                    }
                    "Paid_Expired" -> {
                        binding.timeLeftTxt.text = "Plan Expired"
                        lLTimeLeftText.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@BaseActivity, R.color.color_expired
                                )
                            )
                        timeLeftTxt.setTextColor(
                            ContextCompat.getColor(
                                this@BaseActivity, R.color.text_expired
                            )
                        )
                        binding.syncIcon.setImageDrawable(ContextCompat.getDrawable(
                            this@BaseActivity, R.drawable.ic_not_syncing))
                    }
                    "Trial_Expired" -> {
                        binding.timeLeftTxt.text = "Trial Expired"
                        lLTimeLeftText.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@BaseActivity, R.color.color_expired
                                )
                            )
                        timeLeftTxt.setTextColor(
                            ContextCompat.getColor(
                                this@BaseActivity, R.color.text_expired
                            )
                        )
                        binding.syncIcon.setImageDrawable(ContextCompat.getDrawable(
                            this@BaseActivity, R.drawable.ic_not_syncing))

                    }
                    "Trial_Active" -> {
                        callDetailsViewModel.showExpiryTime.observe({ lifecycle }) {
                            binding.timeLeftTxt.text = "$it days left"

                            lLTimeLeftText.backgroundTintList =
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this@BaseActivity, R.color.green_lighter
                                    )
                                )
                            timeLeftTxt.setTextColor(
                                ContextCompat.getColor(
                                    this@BaseActivity, R.color.green_light
                                )
                            )
                            animateIcon()
                        }
                    }
                }
            }
        }

        callDetailsViewModel.displayPopUp.observe({ lifecycle }) {
                                val dialog = PermissionDialogFragment()
                    dialog.show(supportFragmentManager, "permission_fragment")
        }

        callDetailsViewModel.alreadySynced.observe({ lifecycle }) { syncType ->
            val dialog = PlansFragment()
            when(syncType){
                "Trial_Active"->{
                    callDetailsViewModel.showExpiryTimeInPlan.observe({lifecycle}){ time ->
                        val bundle = bundleOf("title" to "Trial_Active",
                            "days" to time)
                        dialog.arguments = bundle
                    }

                }
                "Trial_Expired"->{
                    val bundle = bundleOf("title" to "Trial_Expired")
                    dialog.arguments = bundle
                }
                "Paid_Expired"->{
                    val bundle = bundleOf("title" to "Paid_Expired")
                    dialog.arguments = bundle
                }
                "Syncing"->{
                    val bundle = bundleOf("title" to "Syncing")
                    dialog.arguments = bundle
                }
            }


            dialog.show(supportFragmentManager, "permission_fragment")

//            GlobalMethods.showMotionToast(
//                this,
//                "Already Synced.",
//                "You have already synced your call logs to the server.",
//                "success",
//                this
//            )
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun checkCallLogsPermission() {
        //Check if the call logs permission is accepted.
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            //Ask for the Read Call Logs Permission.
            requestReadContactsPermission.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun setupBottomNavigation() {

        val bottomNav = object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                NavigationUI.onNavDestinationSelected(
                    item,
                    Navigation.findNavController(this@BaseActivity, R.id.myNavHostFragment)
                )
                when (item.itemId) {
//                    R.id.leads -> {
//
//                        return true
//                    }

                    R.id.call -> {

                        return true
                    }
                    R.id.conversation -> {

                        return true
                    }
                    R.id.accounts -> {

                        return true
                    }
                }
                return false
            }
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener(bottomNav)

        binding.bottomNavigation.setOnNavigationItemReselectedListener {

        }

    }

    private fun askForMediaProjectionPermission() {
        mgr = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        requestReadMediaProjectionPermission.launch(mgr!!.createScreenCaptureIntent())
    }

    override fun getScreenshotPermissions() {
        TODO("Not yet implemented")
    }

    override fun updateExpiryTime(time: String?) {
        binding.apply {

            if (!time.isNullOrEmpty()) {
                if (time.toInt() >= 0)
                    timeLeftTxt.text = time + " Days Left"
                else {
                    timeLeftTxt.text = "Trial Expired"
                    lLTimeLeftText.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@BaseActivity,
                                R.color.color_expired
                            )
                        )
                    timeLeftTxt.setTextColor(
                        ContextCompat.getColor(
                            this@BaseActivity,
                            R.color.text_expired
                        )
                    )
                }
            } else {
                timeLeftTxt.text = "Expired"
                lLTimeLeftText.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@BaseActivity,
                            R.color.color_expired
                        )
                    )
                timeLeftTxt.setTextColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.text_expired
                    )
                )
            }

            lLTimeLeftText.visibility = View.VISIBLE
        }
    }

    private fun syncCallLogsAndGetExpiryTime() {
        callDetailsViewModel.saveSyncItems()
    }


}