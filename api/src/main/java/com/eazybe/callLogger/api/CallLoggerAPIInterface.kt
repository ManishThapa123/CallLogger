package com.eazybe.callLogger.api

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

    @FormUrlEncoded
    @POST("saveclientcalllogs")
    suspend fun saveClientCallLogs(
        @Field("client_mobile") registeredNumber: String,
        @Field("user_name") userName: String,
        @Field("user_number") userNumber: String,
        @Field("call_time") callTime: String,
        @Field("call_duration") callDuration: String,
        @Field("call_type") callType: String,
        @Field("sync_datetime") syncDateTime: String
    ): Response<SaveCallLogsResponse>


    @FormUrlEncoded
    @POST("registercallyzeruser")
    suspend fun registerUser(
        @Field("client_mobile") registeredNumber: String,
        @Field("client_name") clientName: String,
        @Field("reg_date_time") registryDateAndTime: String,
        @Field("organization_code") organizationCode: String
    ): Response<RegistrationResponse>

    @GET("lastsync")
    suspend fun lastsync(
        @Query("client_mobile") registeredNumber: String
    ): Response<LastSyncResponse>

}