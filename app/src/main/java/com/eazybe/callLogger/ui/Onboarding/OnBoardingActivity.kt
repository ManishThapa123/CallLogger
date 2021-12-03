package com.eazybe.callLogger.ui.Onboarding

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
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
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        hideSystemUIAndNavigation(this)
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_host_Onboarding_container)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, 112)
        } else {
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent.putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                this.packageName)
            startActivity(intent)
        }
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

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("resultCode","$requestCode")
    }
}