package com.eazybe.callLogger.api

import com.eazybe.callLogger.api.models.requests.RegisterRequest
import com.eazybe.callLogger.api.models.requests.SaveCallLogs
import com.eazybe.callLogger.api.models.requests.UpdateOrgRequest
import com.eazybe.callLogger.api.models.responses.GetOrganizationResponse
import com.eazybe.callLogger.api.models.responses.LastSyncResponse
import com.eazybe.callLogger.api.models.responses.RegistrationResponse
import com.eazybe.callLogger.api.models.responses.SaveCallLogsResponse
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
    ): Response<RegistrationResponse>

    @GET("getOrganizationDetails")
    suspend fun getOrganizationDetails(
        @Query("organization_code") orgCode: String?
    ): Response<GetOrganizationResponse>

    @POST("updateuserorganization")
    suspend fun updateUserOrganization(
        @Body updateOrgRequest: UpdateOrgRequest
    ): Response<GetOrganizationResponse>
}