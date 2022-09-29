package com.eazybe.callLogger.ui.Dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.simplemobiletools.commons.extensions.toInt
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

        val missedCallList: ArrayList<SampleEntity> = ArrayList()
        val outGoingCallList: ArrayList<SampleEntity> = ArrayList()
        val incomingCallList: ArrayList<SampleEntity> = ArrayList()
        val rejectedCallList: ArrayList<SampleEntity> = ArrayList()


        callLogsHelper.getAllCallLogs { allCallLogs ->

            val distinctCallLogs: ArrayList<SampleEntity> = ArrayList()
            if (!allCallLogs.isNullOrEmpty()){
                if (allCallLogs.size == 1){
                    distinctCallLogs.addAll(allCallLogs)
                }else{
                    distinctCallLogs.addAll(allCallLogs.distinct() as ArrayList<SampleEntity>)
                }

            }
            //To get all calls Count
            _totalPhoneCalls.value = "${distinctCallLogs.size}"

            distinctCallLogs.forEach { callLog ->
                when(callLog.callType){
                    "1", "101" ->{
                        incomingCallList.add(callLog)
                    }
                    "2", "100" -> {
                        outGoingCallList.add(callLog)
                    }
                    "3"-> {
                        missedCallList.add(callLog)
                    }
                    "5", "10" -> {
                        rejectedCallList.add(callLog)
                    }
                }
            }
            //fixed
            val totalOutGoingCallsCount = outGoingCallList.size
            _totalOutgoingCallsCount.value = "$totalOutGoingCallsCount"


            //fixed
            val totalOutGoingCallsDuration = callLogsHelper.getOutGoingCallsDuration(outGoingCallList)
            _totalOutGoingCallsDuration.value =
                GlobalMethods.convertSeconds(totalOutGoingCallsDuration.toInt())

            //fixed
            val totalIncomingCallsCount = incomingCallList.size
            _totalIncomingCallsCount.value = "$totalIncomingCallsCount"


            //fixed
            val totalIncomingCallsDuration = callLogsHelper.getIncomingCallsDuration(incomingCallList)
            _totalIncomingCallsDuration.value =
                GlobalMethods.convertSeconds(totalIncomingCallsDuration.toInt())
            //fixed
            val totalCallDuration = callLogsHelper.getAllCallsDurations(allCallLogs)
            _totalCallsDuration.value = GlobalMethods.convertSeconds(totalCallDuration.toInt())
            //fixed
            val highestCallerCount = callLogsHelper.getHighestCallerCount(allCallLogs)
            _highestCallerCount.value = highestCallerCount
            //fixed
            val highestCallDuration = callLogsHelper.getHighestCallDuration(allCallLogs)
            _highestDurationCall.value = GlobalMethods.convertSeconds(highestCallDuration.callDuration!!.toInt())

            //Highest Caller for today.
            val highestCaller = callLogsHelper.getHighestCaller(allCallLogs)
            _topCaller.value = if (highestCaller == "null")
                "Unknown"
            else
                highestCaller

            //To get neverAttended calls Count
            _neverAttendedCalls.value = "${missedCallList.size}"

            //To get notPickedUpByClient calls Count
            val notPickedUpByClient = callLogsHelper.notPickedUpByClientCount(outGoingCallList)
            _notPickedUpByClient.value = "$notPickedUpByClient"

            //Total Working hours for today.
            val totalWorkingTime =  GlobalMethods.convertSeconds(callLogsHelper.getWorkingTime(allCallLogs).toInt())
            _totalWorkingHours.value = totalWorkingTime


            _loading.value = false
        }
    }

    fun getDashBoardCountsToday() = viewModelScope.launch {
        _loading.value = true


         val missedCallList: ArrayList<SampleEntity> = ArrayList()
         val outGoingCallList: ArrayList<SampleEntity> = ArrayList()
         val incomingCallList: ArrayList<SampleEntity> = ArrayList()
         val rejectedCallList: ArrayList<SampleEntity> = ArrayList()

        callLogsHelper.getAllCallLogs(true) { allCallLogs ->

            val distinctCallLogs: ArrayList<SampleEntity> = ArrayList()
            if (!allCallLogs.isNullOrEmpty()){
                if (allCallLogs.size == 1){
                    distinctCallLogs.addAll(allCallLogs)
                }else{
                    distinctCallLogs.addAll(allCallLogs.distinct() as ArrayList<SampleEntity>)
                }

            }

            //To get all calls Count
            _totalPhoneCalls.value = "${distinctCallLogs.size}"

            distinctCallLogs.forEach { callLog ->
                when(callLog.callType){
                    "1", "101" ->{
                        incomingCallList.add(callLog)
                    }
                    "2", "100" -> {
                        outGoingCallList.add(callLog)
                    }
                    "3"-> {
                        missedCallList.add(callLog)
                    }
                    "5", "10" -> {
                        rejectedCallList.add(callLog)
                    }
                }
            }

            //fixed
            val totalOutGoingCallsCount = outGoingCallList.size
            _totalOutgoingCallsCount.value = "$totalOutGoingCallsCount"


            //fixed
            val totalOutGoingCallsDuration = callLogsHelper.getOutGoingCallsDuration(outGoingCallList)
            _totalOutGoingCallsDuration.value =
                GlobalMethods.convertSeconds(totalOutGoingCallsDuration.toInt())

            //fixed
            val totalIncomingCallsCount = incomingCallList.size
            _totalIncomingCallsCount.value = "$totalIncomingCallsCount"


            //fixed
            val totalIncomingCallsDuration = callLogsHelper.getIncomingCallsDuration(incomingCallList)
            _totalIncomingCallsDuration.value =
                GlobalMethods.convertSeconds(totalIncomingCallsDuration.toInt())
            //fixed
            val totalCallDuration = callLogsHelper.getAllCallsDurations(allCallLogs)
            _totalCallsDuration.value = GlobalMethods.convertSeconds(totalCallDuration.toInt())
            //fixed
            val highestCallerCount = callLogsHelper.getHighestCallerCount(allCallLogs)
            _highestCallerCount.value = highestCallerCount
            //fixed
            val highestCallDuration = callLogsHelper.getHighestCallDuration(allCallLogs)
            _highestDurationCall.value = GlobalMethods.convertSeconds(highestCallDuration.callDuration!!.toInt())

            //Highest Caller for today.
            val highestCaller = callLogsHelper.getHighestCaller(allCallLogs)
            _topCaller.value = if (highestCaller == "null")
                "Unknown"
            else
                highestCaller

            //To get neverAttended calls Count
            _neverAttendedCalls.value = "${missedCallList.size}"

            //To get notPickedUpByClient calls Count
            val notPickedUpByClient = callLogsHelper.notPickedUpByClientCount(outGoingCallList)
            _notPickedUpByClient.value = "$notPickedUpByClient"

            //Total Working hours for today.
            val totalWorkingTime =  GlobalMethods.convertSeconds(callLogsHelper.getWorkingTime(allCallLogs).toInt())
            _totalWorkingHours.value = totalWorkingTime


            _loading.value = false
        }

    }

    fun saveCallLogState() = viewModelScope.launch {
        preferenceManager.saveCallLogAccessState(true)
    }

    fun getCallLogAccessState(): Boolean {
        return preferenceManager.getCallLogAccessState()
    }
}