package com.eazybe.callLogger.repository

import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.api.models.requests.*
import com.eazybe.callLogger.api.models.responses.*
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

    suspend fun registerUser(registerRequest: RegisterRequest): CreateOrUpdateUserResponse? {
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

    suspend fun sendEmailOtp(userEmail: String): StatusResponse? {
        val verifyEmail = apiService.sendEmailOtp(SendOtpEmail(userEmail))
        return verifyEmail.body()
    }

    suspend fun verifyEmailOtp(verifyEmailOtp: VerifyEmailOtp): VerifyOTPResponse? {
        val verifyOtp = apiService.verifyEmailOtp(verifyEmailOtp)
        return verifyOtp.body()
    }

    suspend fun verifyGmailSignUp(verifyGmail: GoogleSignUpRequest): VerifyOTPResponse? {
        val verifyOtp = apiService.verifyGoogleSignUp(verifyGmail)
        return verifyOtp.body()
    }

    suspend fun getAllCustomerFollowups(userMobile: String): AllCustomerFollowUpResponse? {
        val followUps = apiService.getAllCustomerFollowups(userMobile)
        return followUps.body()
    }
}