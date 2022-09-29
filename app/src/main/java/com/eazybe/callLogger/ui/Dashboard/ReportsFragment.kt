package com.eazybe.callLogger.ui.Dashboard

import android.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.databinding.ReportsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.reports_fragment.*

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private lateinit var binding: ReportsFragmentBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val dateArray = arrayOf("Today", "All Time")
    private var selectedSpinner: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReportsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        when (selectedSpinner) {
            "Today" -> {
                dashboardViewModel.getDashBoardCountsToday()
            }
            "All" -> {
                dashboardViewModel.getDashBoardCounts()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        //Call it here phonebook.
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                hideProgressBar()
                if (dashboardViewModel.getCallLogAccessState()) {
                    binding.imgSyncCalls.visibility = View.GONE
                    binding.clDashBoard.visibility = View.VISIBLE
                    val arrayAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.simple_spinner_dropdown_item,
                            dateArray)
                    binding.dateDropDown.adapter = arrayAdapter
                    binding.dateDropDown.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                when (dateArray[p2]) {
                                    "Today" -> {
                                        dashboardViewModel.getDashBoardCountsToday()
                                        selectedSpinner = "Today"
                                    }
                                    "All Time" -> {
                                        dashboardViewModel.getDashBoardCounts()
                                        selectedSpinner = "All"
                                    }
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }
                } else {
                    binding.imgSyncCalls.visibility = View.VISIBLE
                    binding.clDashBoard.visibility = View.GONE
                }


            }, 300)
        } catch (e: Exception) {

        }
    }

    private fun observeViewModel() {
        dashboardViewModel.apply {

            loading.observe({lifecycle}){
                if (it){
                    binding.pbProgress2.visibility = View.VISIBLE
                }else{
                    binding.pbProgress2.visibility = View.GONE
                }
            }
            totalWorkingHours.observe({ lifecycle }) {
                binding.workingTime.text = it

            }
            topCaller.observe({ lifecycle }) {
                binding.topCallerName.text = it
            }
            totalPhoneCalls.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.totalPhoneCalls.text = it + " calls"
                else
                    binding.totalPhoneCalls.text = it + " call"
            }
            neverAttendedCalls.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.neverAttended.text = it + " calls"
                else
                    binding.neverAttended.text = it + " call"
            }
            notPickedUpByClient.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.notPickedUpByClient.text = it + " calls"
                else
                    binding.notPickedUpByClient.text = it + " call"
            }

            totalOutgoingCallsCount.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.totalOutgoingCalls.text = it + " calls"
                else
                    binding.totalOutgoingCalls.text = it + " call"
            }
            totalOutGoingCallsDuration.observe({ lifecycle }) {
                binding.totalOutgoingDuration.text = it
            }
            totalIncomingCallsCount.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.totalIncomingCalls.text = it + " calls"
                else
                    binding.totalIncomingCalls.text = it + " call"
            }
            totalIncomingCallsDuration.observe({ lifecycle }) {
                binding.totalIncomingDuration.text = it
            }
            highestCallerCount.observe({ lifecycle }) {
                if (it.toInt() > 1)
                    binding.topCallerCallsCount.text = it + " calls"
                else
                    binding.topCallerCallsCount.text = it + " call"

            }
            highestDurationCall.observe({ lifecycle }) {
                binding.highestCallDuration.text = it
            }

            totalCallsDuration.observe({ lifecycle }) {
                binding.totalCallTime.text = it
            }
        }
    }

    private fun showProgressBar() {
        binding.apply {
            clDashBoard.visibility = View.GONE
            pbProgress2.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            clDashBoard.visibility = View.VISIBLE
            pbProgress2.visibility = View.GONE
        }
    }
}