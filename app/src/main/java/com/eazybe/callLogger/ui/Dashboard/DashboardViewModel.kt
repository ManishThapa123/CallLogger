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
    private val callLogsHelper: CallLogsHelper
) : ViewModel() {

    private val _totalWorkingHours = MutableLiveData<String>()
    val totalWorkingHours: LiveData<String> = _totalWorkingHours

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _totalPhoneCalls = MutableLiveData<String>()
    val totalPhoneCalls: LiveData<String> = _totalPhoneCalls

    private val _neverAttendedCalls = MutableLiveData<String>()
    val neverAttendedCalls: LiveData<String> = _neverAttendedCalls

    private val _notPickedUpByClient = MutableLiveData<String>()
    val notPickedUpByClient: LiveData<String> = _notPickedUpByClient

    private val _topCaller = MutableLiveData<String>()
    val topCaller: LiveData<String> = _topCaller

    private val _totalOutgoingCallsCount = MutableLiveData<String>()
    val totalOutgoingCallsCount: LiveData<String> = _totalOutgoingCallsCount

    private val _totalIncomingCallsCount = MutableLiveData<String>()
    val totalIncomingCallsCount: LiveData<String> = _totalIncomingCallsCount

    private val _totalCallsDuration = MutableLiveData<String>()
    val totalCallsDuration: LiveData<String> = _totalCallsDuration

    private val _totalIncomingCallsDuration = MutableLiveData<String>()
    val totalIncomingCallsDuration: LiveData<String> = _totalIncomingCallsDuration

    private val _totalOutGoingCallsDuration = MutableLiveData<String>()
    val totalOutGoingCallsDuration: LiveData<String> = _totalOutGoingCallsDuration

    private val _highestDurationCall = MutableLiveData<String>()
    val highestDurationCall: LiveData<String> = _highestDurationCall

    private val _highestCallerCount = MutableLiveData<String>()
    val highestCallerCount: LiveData<String> = _highestCallerCount

    fun getDashBoardCounts() = viewModelScope.launch {
        _loading.value = true
        var totalOutGoings: Int? = 0
        var totalIncomings: Int? = 0
        var totalMissed: Int? = 0


        callLogsHelper.getAllCallLogs {
            val totalOutGoingCallsCount = callLogsHelper.getTotalOutGoingCallsCount()
            _totalOutgoingCallsCount.value = "$totalOutGoingCallsCount"
            totalOutGoings = totalOutGoingCallsCount
            Log.d("totalOutGoingCallsCount", "$totalOutGoingCallsCount")

            val totalOutGoingCallsDuration = callLogsHelper.getTotalOutGoingCallsDuration()
            _totalOutGoingCallsDuration.value =
                GlobalMethods.convertSeconds(totalOutGoingCallsDuration.toInt())
            Log.d("totalOutGoingCallsDuration", totalOutGoingCallsDuration)
            val totalIncomingCallsCount = callLogsHelper.getTotalIncomingCallsCount()
            _totalIncomingCallsCount.value = "$totalIncomingCallsCount"
            totalIncomings = totalIncomingCallsCount

            Log.d("totalIncomingCallsCount", "$totalIncomingCallsCount")

            val totalIncomingCallsDuration = callLogsHelper.getTotalIncomingCallsDuration()
            _totalIncomingCallsDuration.value =
                GlobalMethods.convertSeconds(totalIncomingCallsDuration.toInt())
            Log.d("totalIncomingCallsDuration", totalIncomingCallsDuration)

            val totalCallDuration = callLogsHelper.getTotalCallsDuration()
            _totalCallsDuration.value = GlobalMethods.convertSeconds(totalCallDuration.toInt())

            Log.d("totalCallsDuration", totalCallDuration)

            callLogsHelper.highestCallDuration {
                Log.d("highestDurationCall", "${it.callDuration}, ${it.userName}")
                _highestDurationCall.value = GlobalMethods.convertSeconds(it.callDuration!!.toInt())
            }

            callLogsHelper.highestCallerCallCount {
                Log.d("highestCallerCount", it)
                _highestCallerCount.value = it
            }
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
            totalPhoneCalls = "${it.size}"

            _neverAttendedCalls.value = "${callLogsHelper.getNeverAttendedCallsCount()}"
            totalMissed = callLogsHelper.getNeverAttendedCallsCount()

            _notPickedUpByClient.value = "${callLogsHelper.getTotalNotPickedUpByClientCount()}"

            _totalPhoneCalls.value = "${totalOutGoings!! + totalIncomings!! + totalMissed!!}"
            _loading.value = false

        }
    }

    fun getDashBoardCountsToday() = viewModelScope.launch {
        _loading.value = true
        var totalOutGoings: Int? = 0
        var totalIncomings: Int? = 0
        var totalMissed: Int? = 0

        callLogsHelper.getAllCallLogs {
            val totalOutGoingCallsCount = callLogsHelper.getTotalOutGoingCallsCount(true)
            _totalOutgoingCallsCount.value = "$totalOutGoingCallsCount"
            totalOutGoings = totalOutGoingCallsCount

            val totalOutGoingCallsDuration = callLogsHelper.getTotalOutGoingCallsDuration(true)
            _totalOutGoingCallsDuration.value =
                GlobalMethods.convertSeconds(totalOutGoingCallsDuration.toInt())

            val totalIncomingCallsCount = callLogsHelper.getTotalIncomingCallsCount(true)
            _totalIncomingCallsCount.value = "$totalIncomingCallsCount"
            totalIncomings = totalIncomingCallsCount

            val totalIncomingCallsDuration = callLogsHelper.getTotalIncomingCallsDuration(true)
            _totalIncomingCallsDuration.value =
                GlobalMethods.convertSeconds(totalIncomingCallsDuration.toInt())

            val totalCallDuration = callLogsHelper.getTotalCallsDuration(true)
            _totalCallsDuration.value = GlobalMethods.convertSeconds(totalCallDuration.toInt())

            Log.d("totalCallsDurationToday", totalCallDuration)
            Log.d("totalOutGoingCallsCountToday", "$totalOutGoingCallsCount")
            Log.d("totalOutGoingCallsDurationToday", totalOutGoingCallsDuration)
            Log.d("totalIncomingCallsCountToday", "$totalIncomingCallsCount")
            Log.d("totalIncomingCallsDurationToday", totalIncomingCallsDuration)

            callLogsHelper.highestCallerCallCount(true) {
                Log.d("highestCallerCountToday", it)
                _highestCallerCount.value = it
            }
            callLogsHelper.highestCallDuration(true) {
                Log.d("highestDurationCallToday", "${it.callDuration}, ${it.userName}")
                _highestDurationCall.value = GlobalMethods.convertSeconds(it.callDuration!!.toInt())

            }
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

            //To get neverAttended calls Count
            _neverAttendedCalls.value = "${callLogsHelper.getNeverAttendedCallsCount(true)}"
            totalMissed = callLogsHelper.getNeverAttendedCallsCount(true)
            //To get notPickedUpByClient calls Count
            _notPickedUpByClient.value = "${callLogsHelper.getTotalNotPickedUpByClientCount(true)}"

            //To get all calls Count
            _totalPhoneCalls.value = "${totalOutGoings!! + totalIncomings!! + totalMissed!!}"
            _loading.value = false
//        callLogsHelper.getAllCallLogs(true) {
//            _totalPhoneCalls.value = "${it.size}"
//        }
        }

    }

    fun saveCallLogState() = viewModelScope.launch {
        preferenceManager.saveCallLogAccessState(true)
    }

    fun getCallLogAccessState(): Boolean {
        return preferenceManager.getCallLogAccessState()
    }
}