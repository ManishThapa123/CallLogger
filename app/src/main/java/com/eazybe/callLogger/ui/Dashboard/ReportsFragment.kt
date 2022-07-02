package com.eazybe.callLogger.ui.Dashboard

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.databinding.ReportsFragmentBinding
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private lateinit var binding: ReportsFragmentBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val dateArray = arrayOf("Today", "All Time")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReportsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dashboardViewModel.getCallLogAccessState()) {
            binding.imgSyncCalls.visibility = View.GONE
            binding.clDashBoard.visibility = View.VISIBLE
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, dateArray)
            binding.dateDropDown.adapter = arrayAdapter
            binding.dateDropDown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
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
        } else {
            binding.imgSyncCalls.visibility = View.VISIBLE
            binding.clDashBoard.visibility = View.GONE
        }


        observeViewModel()

        binding.ibBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun observeViewModel() {
        dashboardViewModel.apply {
            totalWorkingHours.observe({ lifecycle }) {
                binding.workingTime.setCharacterLists(TickerUtils.provideNumberList())
                binding.workingTime.text = it

            }
            topCaller.observe({ lifecycle }) {
                binding.topCallerName.setCharacterLists(TickerUtils.provideAlphabeticalList())
                binding.topCallerName.text = it
            }
            totalPhoneCalls.observe({ lifecycle }) {
                binding.totalPhoneCalls.setCharacterLists(TickerUtils.provideNumberList())
                if (it.toInt() > 1)
                    binding.totalPhoneCalls.text = it + " calls"
                else
                    binding.totalPhoneCalls.text = it + " call"
            }
            neverAttendedCalls.observe({ lifecycle }) {
                binding.neverAttended.setCharacterLists(TickerUtils.provideNumberList())
                if (it.toInt() > 1)
                    binding.neverAttended.text = it + " calls"
                else
                    binding.neverAttended.text = it + " call"
            }
            notPickedUpByClient.observe({ lifecycle }) {
                binding.notPickedUpByClient.setCharacterLists(TickerUtils.provideNumberList())
                if (it.toInt() > 1)
                    binding.notPickedUpByClient.text = it + " calls"
                else
                    binding.notPickedUpByClient.text = it + " call"
            }

            totalOutgoingCallsCount.observe({ lifecycle }) {
                binding.totalOutgoingCalls.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalOutgoingCalls.setCharacterLists(TickerUtils.provideAlphabeticalList())
                if (it.toInt() > 1)
                    binding.totalOutgoingCalls.text = it + " calls"
                else
                    binding.totalOutgoingCalls.text = it + " call"
            }
            totalOutGoingCallsDuration.observe({ lifecycle }) {
                binding.totalOutgoingDuration.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalOutgoingDuration.setCharacterLists(TickerUtils.provideAlphabeticalList())
                binding.totalOutgoingDuration.text = it
            }
            totalIncomingCallsCount.observe({ lifecycle }) {
                binding.totalIncomingCalls.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalIncomingCalls.setCharacterLists(TickerUtils.provideAlphabeticalList())
                if (it.toInt() > 1)
                    binding.totalIncomingCalls.text = it + " calls"
                else
                    binding.totalIncomingCalls.text = it + " call"
            }
            totalIncomingCallsDuration.observe({ lifecycle }) {
                binding.totalIncomingDuration.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalIncomingDuration.setCharacterLists(TickerUtils.provideAlphabeticalList())
                binding.totalIncomingDuration.text = it
            }
            highestCallerCount.observe({ lifecycle }) {
                binding.topCallerCallsCount.setCharacterLists(TickerUtils.provideNumberList())
                binding.topCallerCallsCount.setCharacterLists(TickerUtils.provideAlphabeticalList())
                if (it.toInt() > 1)
                    binding.topCallerCallsCount.text = it + " calls"
                else
                    binding.topCallerCallsCount.text = it + " call"

            }
            highestDurationCall.observe({ lifecycle }) {
                binding.highestCallDuration.setCharacterLists(TickerUtils.provideNumberList())
                binding.highestCallDuration.setCharacterLists(TickerUtils.provideAlphabeticalList())
                binding.highestCallDuration.text = it
            }

            totalCallsDuration.observe({ lifecycle }) {
                binding.totalCallTime.setCharacterLists(TickerUtils.provideNumberList())
                binding.totalCallTime.setCharacterLists(TickerUtils.provideAlphabeticalList())
                binding.totalCallTime.text = it
            }
        }


    }


}