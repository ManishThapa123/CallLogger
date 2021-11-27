package com.eazybe.callLogger.ui.Dashboard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.robinhood.ticker.TickerUtils
import com.eazybe.callLogger.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val dateArray = arrayOf("Today", "All Time")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dateArray)
        binding.dateDropDown.adapter = arrayAdapter
        binding.dateDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (dateArray[p2]) {
                    "Today" -> {
                        dashboardViewModel.getDashBoardCountsToday()
                    }
                    "All Time" -> {
                        dashboardViewModel.getDashBoardCounts()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        observeViewModel()

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        dashboardViewModel.apply {
            totalWorkingHours.observe({ lifecycle }) {
                binding.totalWorkingHours.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalWorkingHours.text = it
            }
            topCaller.observe({ lifecycle }) {
                binding.topCaller.text = it
            }
            totalPhoneCalls.observe({ lifecycle }) {
                binding.totalPhoneCalls.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalPhoneCalls.text = it+" calls"
            }
            neverAttendedCalls.observe({ lifecycle }) {
                binding.neverAttended.setCharacterLists(TickerUtils.provideNumberList())
                binding.neverAttended.text = it+" calls"
            }
            notPickedUpByClient.observe({ lifecycle }) {
                binding.notPickedUpByClient.setCharacterLists(TickerUtils.provideNumberList())
                binding.notPickedUpByClient.text = it+" calls"
            }
        }


    }
}