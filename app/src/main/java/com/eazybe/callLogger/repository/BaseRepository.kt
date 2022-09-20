package com.eazybe.callLogger.repository

import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.api.models.requests.*
import com.eazybe.callLogger.api.models.responses.*
import com.eazybe.callLogger.api.models.responses.QuickReplies.CreateQuickReplyResponse
import com.eazybe.callLogger.api.models.responses.QuickReplies.QuickRepliesResponse
import com.eazybe.callLogger.helper.PreferenceManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
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

    suspend fun saveSyncItems(saveSyncCallsRequest: SaveSyncCallsRequestItem): SaveSyncItemResponse? {

        val syncList = ArrayList<SaveSyncCallsRequestItem>()
        syncList.add(saveSyncCallsRequest)

        val syncItems = apiService.saveSyncItems(syncList)
        return syncItems.body()
    }

    suspend fun getLastSynced(workspaceId: String): GetLastSyncedResponse? {
        val lastSynced = apiService.getLastSynced(workspaceId)
        return lastSynced.body()
    }

    suspend fun updateUserDetails(
        file: MultipartBody.Part?,
        workspace_id: Int,
        name: RequestBody
    ): UpdateUserDetailsResponse? {

        val userDetails = apiService.updateUserDetails(file, workspace_id, name)
        return userDetails.body()
    }

    suspend fun fetchUserQuickReply(
        workspaceId: String
    ): QuickRepliesResponse? {
        val quickReplies = apiService.fetchUserQuickReply(workspaceId)
        return quickReplies.body()
    }

    suspend fun downloadFileWithDynamicUrl(url: String): Response<ResponseBody> {
        return apiService.downloadFileWithDynamicUrl(url)
    }

    suspend fun createNewQuickReplyMessage(
        profilePic: MultipartBody.Part?,
        quickReplyTitle: String,
        quickReplyText: String,
        workspaceId: String
    ): CreateQuickReplyResponse?{

        val quickReply = apiService.createNewQuickReplyMessage(profilePic,quickReplyTitle,quickReplyText,workspaceId)
        return quickReply.body()
    }

    suspend fun createNewQuickReplyMessageWithoutPhoto(
        quickReplyTitle: String,
        quickReplyText: String,
        workspaceId: String
    ): CreateQuickReplyResponse?{

        val quickReply = apiService.createNewQuickReplyMessageWithoutPhoto(quickReplyTitle,quickReplyText,workspaceId)
        return quickReply.body()
    }
}