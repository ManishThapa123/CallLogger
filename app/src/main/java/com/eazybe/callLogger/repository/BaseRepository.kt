package com.eazybe.callLogger.repository

import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.api.models.requests.RegisterRequest
import com.eazybe.callLogger.api.models.requests.SaveCallLogs
import com.eazybe.callLogger.api.models.requests.UpdateOrgRequest
import com.eazybe.callLogger.api.models.responses.GetOrganizationResponse
import com.eazybe.callLogger.api.models.responses.RegistrationResponse
import com.eazybe.callLogger.api.models.responses.SaveCallLogsResponse
import com.eazybe.callLogger.helper.PreferenceManager
import javax.inject.Inject

class BaseRepository @Inject constructor(
    private val apiService: CallLoggerAPIInterface,
    private val preferenceManager: PreferenceManager
) {

    suspend fun updateClientCallLog(saveCallLogsRequest: SaveCallLogs): SaveCallLogsResponse? {
        val updateCallLog = apiService.saveClientCallLogs(saveCallLogsRequest)
        return updateCallLog.body()
    }

    suspend fun registerUser(registerRequest: RegisterRequest): RegistrationResponse? {
        val registerUser =
            apiService.registerUser(registerRequest)
        return registerUser.body()
    }

    suspend fun getOrganizationDetails(
        organizationCode: String
    ): GetOrganizationResponse? {
        val orgDetails = apiService.getOrganizationDetails(organizationCode)
        return orgDetails.body()
    }

    suspend fun checkLastSync(registeredNumber: String) = apiService.lastsync(registeredNumber)

    suspend fun updateUserOrganization(updateUserOrgRequest: UpdateOrgRequest): GetOrganizationResponse? {
        val updateUserOrgResponse = apiService.updateUserOrganization(updateUserOrgRequest)
        return updateUserOrgResponse.body()
    }
}