package com.twango.calllogger.repository

import com.twango.callLogger.api.CallLoggerAPIInterface
import com.twango.calllogger.helper.PreferenceManager
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiService: CallLoggerAPIInterface,
    private val preferenceManager: PreferenceManager
) {
    suspend fun updateClientCallLog(
        registeredNumber: String,
        userName: String,
        userNumber: String,
        callTime: String,
        callDuration: String,
        callType: String,
        syncDateTime: String) =
        apiService.saveClientCallLogs(
            registeredNumber,
            userName,
            userNumber,
            callTime,
            callDuration,
            callType,
            syncDateTime)
}