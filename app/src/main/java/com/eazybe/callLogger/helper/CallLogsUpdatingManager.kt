package com.eazybe.callLogger.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eazybe.callLogger.api.models.entities.SampleEntity

object CallLogsUpdatingManager {

    private val _allCallLog = MutableLiveData<Boolean>()
    val allCallLog: LiveData<Boolean> = _allCallLog

    private val _outGoingCallLog = MutableLiveData<SampleEntity>()
    val outGoingCallLog: LiveData<SampleEntity> = _outGoingCallLog

    private val _incomingCallLog = MutableLiveData<SampleEntity>()
    val incomingCallLog: LiveData<SampleEntity> = _incomingCallLog

    private val _missedCallLog = MutableLiveData<SampleEntity>()
    val missedCallLog: LiveData<SampleEntity> = _missedCallLog

    private val _rejectedCallLog = MutableLiveData<SampleEntity>()
    val rejectedCallLog: LiveData<SampleEntity> = _rejectedCallLog

    private val _updateClientNeverPickedUp = MutableLiveData<Boolean>()
    val updateClientNeverPickedUp: LiveData<Boolean> =_updateClientNeverPickedUp

    private val _updateNeverAttended = MutableLiveData<Boolean>()
    val updateNeverAttended: LiveData<Boolean> =_updateNeverAttended

    fun updateExistingCallLogs(type: String, message: String, sampleEntity: SampleEntity) {
        when (type) {
            "1" -> {
                //incoming
                _incomingCallLog.postValue(sampleEntity)
                _allCallLog.postValue(true)
            }
            "2" -> {
                //outgoing
                _outGoingCallLog.postValue(sampleEntity)
                _allCallLog.postValue(true)
                if (sampleEntity.callDuration!!.toInt() == 0)
                    _updateClientNeverPickedUp.postValue(true)
            }
            "3" -> {
                //If required use Lambda function to refresh the call logs list and
                // in the callLogsFragment1 observe the _missed call logs and call the getCallLogs method.
                //missed
                _missedCallLog.postValue(sampleEntity)
                _allCallLog.postValue(true)
                _updateNeverAttended.postValue(true)

            }
            "5","10" -> {
                //rejected
                _rejectedCallLog.postValue(sampleEntity)
                _allCallLog.postValue(true)
            }
        }
    }
}