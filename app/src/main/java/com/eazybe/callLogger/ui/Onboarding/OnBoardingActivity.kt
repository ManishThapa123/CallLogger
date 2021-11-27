package com.eazybe.callLogger.ui.Onboarding

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        hideSystemUIAndNavigation(this)
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_host_Onboarding_container)
//        navController.setGraph(R.navigation.onboarding_navigation)
    }

    override fun onSupportNavigateUp(): Boolean {
        // this will return the control to nav controller to control the back button in toolbar and also device back button.
        // here null is for drawer layout as no drawer layout is used.
        return NavigationUI.navigateUp(navController, null)
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUIAndNavigation(activity: Activity) {
        val window = activity.window

//        window?.apply {
//            setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//            decorView.apply {
//                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                        View.SYSTEM_UI_FLAG_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            }
        window?.apply {
            statusBarColor =
                ContextCompat.getColor(context, R.color.black)
        }

//        }
    }
}