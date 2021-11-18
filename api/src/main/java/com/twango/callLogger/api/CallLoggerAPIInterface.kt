package com.twango.callLogger.api

import com.twango.callLogger.api.models.responses.SaveCallLogsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

}