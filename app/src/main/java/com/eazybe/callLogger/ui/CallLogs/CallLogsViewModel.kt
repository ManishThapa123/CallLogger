package com.eazybe.callLogger.ui.CallLogs

import a.a.b.a.f.f
import android.content.Context
import android.media.projection.MediaProjection
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.entities.CallDetailsWithCount
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.api.models.requests.SaveSyncCallsRequestItem
import com.eazybe.callLogger.api.models.responses.UserData
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.AmplifyHelper
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper,
    private val baseRepository: BaseRepository,
    private val clientDetailAdapter: JsonAdapter<UserData>,
    private val amplifyHelper: AmplifyHelper,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>
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

    private val _callLogPermissionState = MutableLiveData<Boolean>()
    val callLogPermissionState: LiveData<Boolean> = _callLogPermissionState

    private val _lastSynced = MutableLiveData<String?>()
    val lastSynced: LiveData<String?> = _lastSynced

    private val _getSyncDateFromAPI = MutableLiveData<Boolean>()
    val getSyncDateFromAPI: LiveData<Boolean> = _getSyncDateFromAPI

    private val _alreadySynced = MutableLiveData<String>()
    val alreadySynced: LiveData<String> = _alreadySynced

    private val _displayPopUp = MutableLiveData<Boolean>()
    val displayPopUp: LiveData<Boolean> = _displayPopUp

    private val _lastSyncedTime = MutableLiveData<String?>()
    val lastSyncedTime: LiveData<String?> = _lastSyncedTime

    private val _showExpiryTime = MutableLiveData<String?>()
    val showExpiryTime: LiveData<String?> = _showExpiryTime

    private val _showExpiryTimeInPlan = MutableLiveData<String?>()
    val showExpiryTimeInPlan: LiveData<String?> = _showExpiryTimeInPlan

    private val _updateExpiryTime = MutableLiveData<String?>()
    val updateExpiryTime: LiveData<String?> = _updateExpiryTime

    fun getCallLogs(type: String, useHandler: Boolean? = false) = viewModelScope.launch {
        when (type) {
            "1" -> {
                callLogsHelper.getAllCallLogs { callDetails ->
                    Log.d("AlCallDetails", "$callDetails")
                    _sampleData.value = callDetails

                    if (preferenceManager.getSyncState())
                        amplifyHelper.checkUserLastSynced(callDetails)

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

    fun saveMediaProjectionToken(mediaProjection: MediaProjection) = viewModelScope.launch {

    }

    fun saveRegisteredDateAndTime() = viewModelScope.launch {
        //Check Preferences for the saved Time.
        if (!preferenceManager.isSavedFirstRegisterTimeStamp()) {

            val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()
            val convertedUserData = clientDetailEmailAdapter.fromJson(prefSavedUserData!!)


            Log.d("RegisteredUserTime", "${convertedUserData?.loggingStartsFrom}")
            val finalConvertedDateInMillis =
                GlobalMethods.getMilliFromDate(
                    if (!convertedUserData?.loggingStartsFrom.isNullOrEmpty()) {
                        convertedUserData?.loggingStartsFrom
                    } else {
                        "2022-01-01 20:20:20"
                    }
                )
            preferenceManager.saveFirstTimeRegisterMillis(finalConvertedDateInMillis)

            val finalConvertedSyncedTimeInMillis =
                GlobalMethods.getMilliFromDate(convertedUserData?.lastSynced)
            preferenceManager.saveLastSyncedTimeInMillis(finalConvertedSyncedTimeInMillis)
        }
    }

    //to get the synced time from the time the user has started syncing the call logs.
    fun getSyncedTimeAndSaveInPreference(firstTime: Boolean = false) = viewModelScope.launch {

        val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()
        val convertedUserData = clientDetailEmailAdapter.fromJson(prefSavedUserData!!)

        baseRepository.getLastSynced("${convertedUserData?.id}")?.let { response ->
            if (response.type == true) {
                val time =
                    GlobalMethods.convertSyncedDateToMillisGetSync("${response.syncData?.get(0)?.syncStartedAt}")
                Log.d("time2", "$time")
                preferenceManager.saveLastSyncedTime(time)

                when ("${response.syncData?.get(0)?.isActive}") {
                    "0" -> {
                        preferenceManager.saveSyncState(true)

                        val timeRemaining = calculateTimeRemaining(
                            "${response.syncData?.get(0)?.syncStartedAt}",
                            "${response.syncData?.get(0)?.vailidity}")

                        if (timeRemaining.toInt() >= 0) {
                            _lastSyncedTime.value = "Trial_Active"
                            preferenceManager.saveExpiryState("Trial_Active")
                            preferenceManager.saveExpiryTime(timeRemaining)
                            _showExpiryTime.value = timeRemaining

                            if (firstTime)
                                _displayPopUp.value = true
                            //introduce a new variable
                            //unpaid_syncing
                        }
                        else {
                            _lastSyncedTime.value = "Trial_Expired"
                            preferenceManager.saveExpiryState("Trial_Expired")
                            preferenceManager.saveExpiryTime("0")
                        }
                    }
                    "1" -> {
                        preferenceManager.saveSyncState(false)
                        _lastSyncedTime.value = "Paid_Expired"
                        preferenceManager.saveExpiryState("Paid_Expired")
                        preferenceManager.saveExpiryTime("0")

                    }
                    "2" -> {
                        preferenceManager.saveSyncState(true)
                        _lastSyncedTime.value = "Syncing"
                        preferenceManager.saveExpiryState("Syncing")
                        preferenceManager.saveExpiryTime("0")
                    }
                }



//                preferenceManager.saveWhatsAppExpiry("-1")


            } else {
                _lastSyncedTime.value = "Not_Synced"
                //Sync not started
            }
        }
    }

    private fun calculateTimeRemaining(startDate: String, validity: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val cal: Calendar = Calendar.getInstance()
        val cal2: Calendar = Calendar.getInstance()
        try {
            cal.time = sdf.parse(startDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        cal.add(
            Calendar.DATE,
            validity.toInt()) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        cal2.add(
            Calendar.DATE,
            0)
//        val expiryDate: String = sdf.format(cal.time)
//        val currentDate = cal.add(Calendar.DATE, 0)
        println("Cal Values: ${cal.timeInMillis}  and ${cal2.timeInMillis}")
        val diff: Long = cal.timeInMillis - cal2.timeInMillis
        println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
        return "${TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)}"
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

    fun saveCallLogState() = viewModelScope.launch {
        preferenceManager.saveCallLogAccessState(true)
    }

    fun getCallLogAccessState(): Boolean {
        return preferenceManager.getCallLogAccessState()
    }

    fun saveSyncItems() = viewModelScope.launch {

        //first check it if already exists or not.
        //write the cases accordingly to set the timer. if data exists save time in pref

        //To start syncing the call logs.
        val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()
        val convertedUserData = clientDetailEmailAdapter.fromJson(prefSavedUserData!!)
        val date = GlobalMethods.createDateToday()

        baseRepository.getLastSynced("${convertedUserData?.id}")?.let { response ->
            if (response.type == true) {

                when ("${response.syncData?.get(0)?.isActive}") {
                    "0" -> {
                        preferenceManager.saveSyncState(true)

                        val timeRemaining = calculateTimeRemaining(
                            "${response.syncData?.get(0)?.syncStartedAt}",
                            "${response.syncData?.get(0)?.vailidity}")

                        if (timeRemaining.toInt() >= 0) {
                            _alreadySynced.value = "Trial_Active"
                            _showExpiryTimeInPlan.value = timeRemaining

                        }
                        else {
                            _alreadySynced.value = "Trial_Expired"

                        }
                    }
                    "1" -> {
                        _alreadySynced.value = "Paid_Expired"
                    }
                    "2" -> {
                        _alreadySynced.value = "Syncing"

                    }
                }






            } else {
                baseRepository.saveSyncItems(
                    SaveSyncCallsRequestItem(
                        "call",
                        convertedUserData!!.id,
                        date
                    )
                )?.let {
                    Log.d("response", "${it.data}")

//                    val time =
//                        GlobalMethods.convertSyncedDateToMillis(it.data?.get(0)?.syncStartedAt!!)
//
//                    Log.d("time", "$time")
//            preferenceManager.saveLastSyncedTime(it.data?.get(0)?.syncStartedAt!!)
                    _getSyncDateFromAPI.value = true
                }
            }
        }
    }
}