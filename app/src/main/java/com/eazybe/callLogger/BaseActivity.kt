package com.eazybe.callLogger

import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.eazybe.callLogger.databinding.ActivityBaseBinding
import com.eazybe.callLogger.extensions.toast
import com.eazybe.callLogger.interfaces.ScreenshotInterface
import com.eazybe.callLogger.keyboard.view.MyKeyboardView
import com.eazybe.callLogger.ui.CallLogs.CallLogsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * New Architecture along with the bottom navigation
 */
@AndroidEntryPoint
class BaseActivity : AppCompatActivity(),ScreenshotInterface {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var navController: NavController
    private val callDetailsViewModel: CallLogsViewModel by viewModels()
    private var mgr: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var keyboardView: MyKeyboardView? = null

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

        keyboardView = MyKeyboardView(this,null)
        keyboardView!!.setListener(this)
        //Finding the Navigation Controller
        val navController = findNavController(R.id.myNavHostFragment)

        //Setting Navigation Controller with the BottomNavigationView
        binding.bottomNavigation.setupWithNavController(navController)

        setupBottomNavigation()
        binding.searchBtn
//        askForMediaProjectionPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
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
                    R.id.leads -> {

                        return true
                    }

                    R.id.followUp -> {

                        return true
                    }

                    R.id.call -> {

                        return true
                    }
                    R.id.reports -> {

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

}