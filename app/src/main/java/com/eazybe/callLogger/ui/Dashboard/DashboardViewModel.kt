package com.eazybe.callLogger.ui.Dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper) : ViewModel() {

    private val _totalWorkingHours = MutableLiveData<String>()
    val totalWorkingHours: LiveData<String> = _totalWorkingHours

    private val _totalPhoneCalls = MutableLiveData<String>()
    val totalPhoneCalls: LiveData<String> = _totalPhoneCalls

    private val _neverAttendedCalls = MutableLiveData<String>()
    val neverAttendedCalls: LiveData<String> = _neverAttendedCalls

    private val _notPickedUpByClient = MutableLiveData<String>()
    val notPickedUpByClient: LiveData<String> = _notPickedUpByClient

    private val _topCaller = MutableLiveData<String>()
    val topCaller: LiveData<String> = _topCaller

    fun getDashBoardCounts() = viewModelScope.launch {
        callLogsHelper.getHighestCallLogsCount {
            it.forEach { callDetailsWithCount ->
                Log.d(
                    "allCallsHighest",
                    "${callDetailsWithCount.callDetails?.userName} and ${callDetailsWithCount.count}"
                )
            }
        }
        //Highest Caller for today.
        callLogsHelper.highestCaller { highestCaller ->
            Log.d("highest", highestCaller)
            _topCaller.value = if (highestCaller == "null")
                "Unknown"
            else
                highestCaller
        }
        //Total Working hours for today.
        _totalWorkingHours.value =
            GlobalMethods.convertSeconds(callLogsHelper.getAllCallsDuration().toInt())
        var totalPhoneCalls = ""
        callLogsHelper.getAllCallLogs {
            totalPhoneCalls = "${it.size}"
        }
        _totalPhoneCalls.value = totalPhoneCalls
        _neverAttendedCalls.value = "${callLogsHelper.getNeverAttendedCallsCount()}"
        _notPickedUpByClient.value = "${callLogsHelper.getTotalNotPickedUpByClientCount()}"

    }

    fun getDashBoardCountsToday() = viewModelScope.launch {
        //Total Working hours for today.
        _totalWorkingHours.value =
            GlobalMethods.convertSeconds(callLogsHelper.getAllCallsDuration(true).toInt())
        //Highest Caller for today.
        callLogsHelper.highestCaller(true) { highestCaller ->
            Log.d("highest", highestCaller)
            _topCaller.value = if (highestCaller == "null")
                "Unknown"
            else
                highestCaller
        }
        //Highest Call logs count for today.
        callLogsHelper.getHighestCallLogsCount(true) {
            it.forEach { callDetailsWithCount ->
                Log.d(
                    "allCallsHighest",
                    "${callDetailsWithCount.callDetails?.userName} and ${callDetailsWithCount.count}"
                )
            }
        }
        //To get all calls Count
        callLogsHelper.getAllCallLogs(true) {
            _totalPhoneCalls.value = "${it.size}"
        }
        //To get neverAttended calls Count
        _neverAttendedCalls.value = "${callLogsHelper.getNeverAttendedCallsCount(true)}"
        //To get notPickedUpByClient calls Count
        _notPickedUpByClient.value = "${callLogsHelper.getTotalNotPickedUpByClientCount(true)}"
    }
}