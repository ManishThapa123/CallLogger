package com.twango.calllogger.ui.Dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.helper.CallLogsHelper
import com.twango.calllogger.helper.GlobalMethods
import com.twango.calllogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper
): ViewModel() {

    private val _totalWorkingHours = MutableLiveData<String>()
    val totalWorkingHours: LiveData<String> = _totalWorkingHours

    private val _totalPhoneCalls= MutableLiveData<String>()
    val totalPhoneCalls: LiveData<String> = _totalPhoneCalls

    private val _neverAttendedCalls = MutableLiveData<String>()
    val neverAttendedCalls: LiveData<String> = _neverAttendedCalls

    private val _notPickedUpByClient = MutableLiveData<String>()
    val notPickedUpByClient: LiveData<String> = _notPickedUpByClient

    private val _topCaller = MutableLiveData<String>()
    val topCaller: LiveData<String> = _topCaller


    fun getDashBoardCounts() = viewModelScope.launch {
       callLogsHelper.getHighestCallLogsCount().let {
           it.forEach{ callDetailsWithCount ->
               Log.d("allCallsHighest","${callDetailsWithCount.callDetails?.userName} and ${callDetailsWithCount.count}")
           }
       }
        val highestCaller = callLogsHelper.highestCaller()
        Log.d("highest", highestCaller)
        _topCaller.value = highestCaller
        _totalWorkingHours.value = GlobalMethods.convertSeconds(callLogsHelper.getAllCallsDuration().toInt())
        _totalPhoneCalls.value =  "${callLogsHelper.getAllCallLogs()!!.size}"
        _neverAttendedCalls.value = "${callLogsHelper.getNeverAttendedCallsCount()}"
        _notPickedUpByClient.value = "${callLogsHelper.getTotalNotPickedUpByClientCount()}"

    }

}