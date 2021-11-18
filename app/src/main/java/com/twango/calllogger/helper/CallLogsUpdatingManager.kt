package com.twango.calllogger.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twango.callLogger.api.models.entities.SampleEntity

object CallLogsUpdatingManager {

    private val _allCallLog = MutableLiveData<SampleEntity>()
    val allCallLog: LiveData<SampleEntity> = _allCallLog

    private val _outGoingCallLog = MutableLiveData<SampleEntity>()
    val outGoingCallLog: LiveData<SampleEntity> = _outGoingCallLog

    private val _incomingCallLog = MutableLiveData<SampleEntity>()
    val incomingCallLog: LiveData<SampleEntity> = _incomingCallLog

    private val _missedCallLog = MutableLiveData<SampleEntity>()
    val missedCallLog: LiveData<SampleEntity> = _missedCallLog

    private val _rejectedCallLog = MutableLiveData<SampleEntity>()
    val rejectedCallLog: LiveData<SampleEntity> = _rejectedCallLog


    fun updateExistingCallLogs(type: String, message: String, sampleEntity: SampleEntity) {
        when (type) {
            "1" -> {
                //incoming
                _incomingCallLog.postValue(sampleEntity)
                _allCallLog.postValue(sampleEntity)
            }
            "2" -> {
                //outgoing
                _outGoingCallLog.postValue(sampleEntity)
                _allCallLog.postValue(sampleEntity)
            }
            "3" -> {
                //missed
                _missedCallLog.postValue(sampleEntity)
                _allCallLog.postValue(sampleEntity)
            }
            "5" -> {
                //rejected
                _rejectedCallLog.postValue(sampleEntity)
                _allCallLog.postValue(sampleEntity)
            }
        }
    }
}