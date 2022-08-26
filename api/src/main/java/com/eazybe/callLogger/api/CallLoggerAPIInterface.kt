package com.eazybe.callLogger.api

import com.eazybe.callLogger.api.models.requests.*
import com.eazybe.callLogger.api.models.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 *Request method and URL specified in the annotation
 *Callback for the parsed response is the last parameter
 * Get requests are annotated with @GET along with the parameter, which is the URL
 */
interface CallLoggerAPIInterface {

    @POST("createusercalllogs")
    suspend fun saveClientCallLogs(
        @Body saveCallLogs: SaveCallLogs
    ): Response<SaveCallLogsResponse>

    @GET("lastsync")
    suspend fun lastsync(
        @Query("client_mobile") registeredNumber: String
    ): Response<LastSyncResponse>

    @POST("createCallyzerUser")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<CreateOrUpdateUserResponse>

    @GET("getOrganizationDetails")
    suspend fun getOrganizationDetails(
        @Query("organization_code") orgCode: String?
    ): Response<GetOrganizationResponse>

    @POST("updateuserorganization")
    suspend fun updateUserOrganization(
        @Body updateOrgRequest: UpdateOrgRequest
    ): Response<GetOrganizationResponse>

    @POST("resendEmailOtp")
    suspend fun sendEmailOtp(
        @Body sendOtpEmail: SendOtpEmail
    ): Response<StatusResponse>

    @POST("verifyemailotpworkspace")
    suspend fun verifyEmailOtp(
        @Body verifyEmailOtp: VerifyEmailOtp
    ): Response<VerifyOTPResponse>

    @POST("verifygooglesignupworkspace")
    suspend fun verifyGoogleSignUp(
        @Body googleSignUpRequest: GoogleSignUpRequest
    ): Response<VerifyOTPResponse>


    //Follow Up API
    @GET("allCustomerFollowups")
    suspend fun getAllCustomerFollowups(
        @Query("user_mobile_No") userMobile: String?
    ): Response<AllCustomerFollowUpResponse>

    @POST("savesyncitems")
    suspend fun saveSyncItems(
        @Body saveSyncedCallsRequest: List<SaveSyncCallsRequestItem>
    ): Response<SaveSyncItemResponse>

    @GET("getyncitems")
    suspend fun getLastSynced(
        @Query("workspace_id") workspaceId: String?
    ):Response<GetLastSyncedResponse>


    @PATCH("updatepersonaldetails")
    @Multipart
    suspend fun updateUserDetails(
        @Part profile_pic: MultipartBody.Part?,
        @Part("workspace_id") workspaceId: Int,
        @Part("name") name: RequestBody
    ): Response<UpdateUserDetailsResponse>
}