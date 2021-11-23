package com.twango.calllogger.ui.SignUpAndLogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.twango.calllogger.R
import com.twango.calllogger.databinding.ActivityOnboardingBinding
import com.twango.calllogger.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_host_Registration_container)
    }

    override fun onSupportNavigateUp(): Boolean {
        // this will return the control to nav controller to control the back button in toolbar and also device back button.
        // here null is for drawer layout as no drawer layout is used.
        return NavigationUI.navigateUp(navController, null)
    }

}