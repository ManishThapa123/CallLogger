package com.twango.calllogger.ui.CallLogs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twango.callLogger.api.models.entities.CallDetailsWithCount
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.helper.CallLogsHelper
import com.twango.calllogger.helper.GlobalMethods
import com.twango.calllogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper
) : ViewModel() {

    private val _sampleData = MutableLiveData<List<SampleEntity>>()
    val sampleData: LiveData<List<SampleEntity>> = _sampleData

    private val _missedCallData = MutableLiveData<List<SampleEntity>>()
    val missedCallData: LiveData<List<SampleEntity>> = _missedCallData

    private val _outGoingCallData = MutableLiveData<List<SampleEntity>>()
    val outGoingCallData: LiveData<List<SampleEntity>> = _outGoingCallData

    private val _incomingCallData = MutableLiveData<List<SampleEntity>>()
    val incomingCallData: LiveData<List<SampleEntity>> = _incomingCallData

    private val _rejectedCallData = MutableLiveData<List<SampleEntity>>()
    val rejectedCallData: LiveData<List<SampleEntity>> = _rejectedCallData

    private val _neverAttendedCallData = MutableLiveData<List<CallDetailsWithCount>>()
    val neverAttendedCallData: LiveData<List<CallDetailsWithCount>> = _neverAttendedCallData

    private val _notPickedUpByClientCallData = MutableLiveData<List<CallDetailsWithCount>>()
    val notPickedUpByClientCallData: LiveData<List<CallDetailsWithCount>> =
        _notPickedUpByClientCallData

    private val _permissionState = MutableLiveData<Boolean>()
    val permissionState: LiveData<Boolean> = _permissionState

    private val _lastSynced = MutableLiveData<String?>()
    val lastSynced: LiveData<String?> = _lastSynced


    fun getCallLogs(type: String) = viewModelScope.launch {
        when (type) {
            "1" -> {
                val callDetails = callLogsHelper.getAllCallLogs()
                _sampleData.value = callDetails!!
            }
            "2" -> {
                val callDetails = callLogsHelper.getIncomingCallLogs()
                _incomingCallData.value = callDetails!!
            }
            "3" -> {
                val callDetails = callLogsHelper.getOutGoingCallLogs()
                _outGoingCallData.value = callDetails!!

                val callDuration = callLogsHelper.getOutgoingCallStateDuration("+9779818480892")
                Log.d("callDuration", "$callDuration")
            }
            "4" -> {
                val callDetails = callLogsHelper.getMissedCallLogs()
                _missedCallData.value = callDetails!!
            }
            "5" -> {
                val callDetails = callLogsHelper.getRejectedCallLogs()
                _rejectedCallData.value = callDetails!!
            }
            "6" -> {
                val callDetails = callLogsHelper.getNeverAttended()
                Log.d("neverAttended", "$callDetails")
                _neverAttendedCallData.value = callDetails

            }
            "7" -> {
                val callDetails = callLogsHelper.getNeverPickedUpByClient()
                Log.d("neverPickedUp", "$callDetails")
                _notPickedUpByClientCallData.value = callDetails
                for (item in callDetails) {
                    Log.d(
                        "neverPickedUpDetails",
                        "${item.callDetails?.userName} and ${item.callDetails?.userNumber} ${item.count}"
                    )
                }
            }
        }
    }


    fun getAndSaveLatestSyncedTime() = viewModelScope.launch {
        val currentTimeInMillis = callLogsHelper.createDate(0)
        preferenceManager.saveLastSyncedTimeInMillis(currentTimeInMillis)
        //convert the millis to TimeStamp
        val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
        Log.d("currentTimeConverted", currentTimeConverted)
        //Make an API call to save the time.
    }

    fun getAutoRunPermissionSavedState() = viewModelScope.launch {
        val permissionState = preferenceManager.getSaveAutoStartPermissionStart()
        _permissionState.value = permissionState
    }

    fun saveAutoRunPermissionSavedState() = viewModelScope.launch {
        preferenceManager.saveAutoStartPermissionStart()
    }

    fun getLastSynced() = viewModelScope.launch {
        val lastSynced = preferenceManager.getLastSyncedTimeInMillis()
        _lastSynced.value = lastSynced
    }
}