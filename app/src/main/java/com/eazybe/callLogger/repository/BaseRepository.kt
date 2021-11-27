package com.eazybe.callLogger.repository

import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.helper.PreferenceManager
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

    //In order to register a new User to the app.
    suspend fun registerUser(
        registeredNumber: String,
        clientName: String,
        registryDateAndTime: String,
        organizationCode: String) = apiService.registerUser(registeredNumber, clientName, registryDateAndTime,organizationCode)

    suspend fun checkLastSync(
        registeredNumber: String) = apiService.lastsync(registeredNumber)
}