package com.twango.calllogger.ui.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.twango.calllogger.R
import com.twango.calllogger.databinding.ActivityDashboardBinding
import com.twango.calllogger.databinding.ActivitySplashBinding
import com.twango.calllogger.ui.SplashScreen.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dashboardViewModel.getDashBoardCounts()
        observeViewModel()

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        dashboardViewModel.apply {
           totalWorkingHours.observe({lifecycle}){
                binding.totalWorkingHours.text = it
            }
            topCaller.observe({lifecycle}){
                binding.topCaller.text = it
            }
            totalPhoneCalls.observe({lifecycle}){
                binding.totalPhoneCalls.text = it+" calls"
            }
            neverAttendedCalls.observe({lifecycle}){
                binding.neverAttended.text = it+" calls"
            }
            notPickedUpByClient.observe({lifecycle}){
                binding.notPickedUpByClient.text = it+" calls"
            }
        }


    }
}