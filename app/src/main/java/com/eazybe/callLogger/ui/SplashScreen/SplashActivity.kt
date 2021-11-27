package com.eazybe.callLogger.ui.SplashScreen

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.eazybe.callLogger.MainActivity
import com.eazybe.callLogger.databinding.ActivitySplashBinding
import com.eazybe.callLogger.ui.Onboarding.OnBoardingActivity
import com.eazybe.callLogger.ui.SignUpAndLogin.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    private var isPermissionSaved: Boolean = false
    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        splashViewModel.getPermissionSavedState()
        splashViewModel.getLoginState()
        observeViewModel()

        binding.splashMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

                when {
                    progress > 0.65f -> {
                        binding.imageView.setBackgroundColor(Color.WHITE)

                    }
                    progress > 0.45f -> {
                        binding.imageView2.visibility = View.GONE
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                if (isLoggedIn) {
                    // Start activity
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    // Animate the loading of new activity
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    // Close this activity
                    finish()
                } else if (isPermissionSaved){
                    startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))
                    // Animate the loading of new activity
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    // Close this activity
                    finish()
                }else {
                    // Start activity
                    startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                    // Animate the loading of new activity
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    // Close this activity
                    finish()
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })
    }

    private fun observeViewModel() {
        splashViewModel.permissionState.observe({ lifecycle }) { isSaved ->
            if (isSaved) {
                isPermissionSaved = isSaved
            }
        }
        splashViewModel.loginState.observe({ lifecycle }) { isLoginCredSaved ->
            if (isLoginCredSaved) {
                isLoggedIn = isLoginCredSaved
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}