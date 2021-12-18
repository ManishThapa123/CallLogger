package com.eazybe.callLogger.repository

import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.api.models.requests.RegisterRequest
import com.eazybe.callLogger.api.models.requests.SaveCallLogs
import com.eazybe.callLogger.api.models.requests.UpdateOrgRequest
import com.eazybe.callLogger.helper.PreferenceManager
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiService: CallLoggerAPIInterface,
    private val preferenceManager: PreferenceManager
) {
    suspend fun updateClientCallLog(
        saveCallLogsRequest: SaveCallLogs
    ) = apiService.saveClientCallLogs(saveCallLogsRequest)

    //In order to register a new User to the app.
    suspend fun registerUser(
        registerRequest: RegisterRequest
    ) = apiService.registerUser(registerRequest)

    suspend fun getOrganizationDetails(
        organizationCode: String
    ) = apiService.getOrganizationDetails(organizationCode)

    suspend fun checkLastSync(
        registeredNumber: String
    ) = apiService.lastsync(registeredNumber)

    suspend fun updateUserOrganization(
        updateUserOrgRequest: UpdateOrgRequest
    ) = apiService.updateUserOrganization(updateUserOrgRequest)
}