package com.eazybe.callLogger.ui.CallLogs

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.eazybe.callLogger.api.models.entities.CallDetailsWithCount
import com.eazybe.callLogger.api.models.entities.Data
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper,
    private val clientDetailAdapter: JsonAdapter<Data>
) : ViewModel() {

    private val _sampleData = MutableLiveData<List<SampleEntity>>()
    val sampleData: LiveData<List<SampleEntity>> = _sampleData

    private val _sampleDataUpdated = MutableLiveData<List<SampleEntity>>()
    val sampleDataUpdated: LiveData<List<SampleEntity>> = _sampleDataUpdated

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

    private val _neverAttendedCallDataUpdated = MutableLiveData<List<CallDetailsWithCount>>()
    val neverAttendedCallDataUpdated: LiveData<List<CallDetailsWithCount>> =
        _neverAttendedCallDataUpdated

    private val _notPickedUpByClientCallData = MutableLiveData<List<CallDetailsWithCount>>()
    val notPickedUpByClientCallData: LiveData<List<CallDetailsWithCount>> =
        _notPickedUpByClientCallData

    private val _notPickedUpByClientCallDataUpdated = MutableLiveData<List<CallDetailsWithCount>>()
    val notPickedUpByClientCallDataUpdated: LiveData<List<CallDetailsWithCount>> =
        _notPickedUpByClientCallDataUpdated

    private val _permissionState = MutableLiveData<Boolean>()
    val permissionState: LiveData<Boolean> = _permissionState

    private val _lastSynced = MutableLiveData<String?>()
    val lastSynced: LiveData<String?> = _lastSynced


    fun getCallLogs(type: String, useHandler: Boolean? = false) = viewModelScope.launch {
        when (type) {
            "1" -> {
                callLogsHelper.getAllCallLogs { callDetails ->
                    Log.d("AlCallDetails", "$callDetails")
                    _sampleData.value = callDetails
                }
            }
            "2" -> {
                val callDetails = callLogsHelper.getIncomingCallLogs()
                _incomingCallData.value = callDetails!!

//                callLogsHelper.getIncomingCallLogs{callDetails ->
//                    _incomingCallData.value = callDetails
//                }
            }
            "3" -> {
                callLogsHelper.getOutGoingCallLogs { callDetails ->
                    _outGoingCallData.value = callDetails

                    val callDuration = callLogsHelper.getOutgoingCallStateDuration("+9779818480892")
                    Log.d("callDuration", "$callDuration")
                }

            }
            "4" -> {
                //todo:// change the logic to lambda here as well
                val callDetails = callLogsHelper.getMissedCallLogs()
                _missedCallData.value = callDetails!!
            }
            "5" -> {
                val callDetails = callLogsHelper.getRejectedCallLogs()
                _rejectedCallData.value = callDetails!!
            }
            "6" -> {
                if (useHandler == true) {
                    callLogsHelper.getNeverAttended(true) { callDetails ->
                        Log.d("neverAttended", "$callDetails")
//                        _neverAttendedCallData.value = callDetails
                        _neverAttendedCallDataUpdated.value = callDetails
                    }

                } else {
                    callLogsHelper.getNeverAttended { callDetails ->
                        Log.d("neverAttended", "$callDetails")
                        _neverAttendedCallData.value = callDetails
                    }

                }

            }
            "7" -> {
                if (useHandler == true) {
                    callLogsHelper.getNeverPickedUpByClient(true) { callDetails ->
                        Log.d("neverPickedUpByClientCallList", "$callDetails")
                        _notPickedUpByClientCallDataUpdated.value = callDetails
                    }
                } else {
                    callLogsHelper.getNeverPickedUpByClient { callDetails ->
                        Log.d("neverPickedUpByClientCallList", "$callDetails")
                        _notPickedUpByClientCallData.value = callDetails
                    }
                }
            }
        }
    }

    fun getAndSaveLatestSyncedTime() = viewModelScope.launch {
        //Make an API call to save the time.
        val currentTimeInMillis = callLogsHelper.createDate(0)
        preferenceManager.saveLastSyncedTimeInMillis(currentTimeInMillis)
        //convert the millis to TimeStamp
        val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
        Log.d("currentTimeConverted", currentTimeConverted)

    }

    fun saveRegisteredDateAndTime() = viewModelScope.launch {
        //Check Preferences for the saved Time.
        if (!preferenceManager.isSavedFirstRegisterTimeStamp()) {
            val prefSavedUserData = preferenceManager.getClientRegistrationData()
            val convertedUserData = clientDetailAdapter.fromJson(prefSavedUserData!!)
            Log.d("RegisteredUserTime", "${convertedUserData?.registeredOn}")
            val finalConvertedDateInMillis =
                GlobalMethods.getMilliFromDate(convertedUserData?.registeredOn)
            preferenceManager.saveFirstTimeRegisterMillis(finalConvertedDateInMillis)
        }
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

    fun deleteUserCallLog(callData: SampleEntity, context: Context) {
        callLogsHelper.deleteCallById(callData.callLogId!!, context)
    }
}