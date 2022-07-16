package com.eazybe.callLogger.api

import com.eazybe.callLogger.api.models.responses.AllCustomerFollowUpResponse
import com.eazybe.callLogger.api.models.responses.CreateFollowUpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface KeyBoardApiInterface {

    //Follow Up API
    @GET("allCustomerFollowups")
    suspend fun getAllCustomerFollowups(
        @Query("user_mobile_No") userMobile: String?
    ): Response<AllCustomerFollowUpResponse>

    @POST("createFollowUp")
    suspend fun createFollowUp(
        @Query("user_mobile") userMobile: String?,
        @Query("customer_mobile") customerMobile: String?,
        @Query("chat_Id") chatId: String?,
        @Query("name") name: String?,
        @Query("priority") priority: String?,
        @Query("follow_up_date") followUpDate: String?,
        @Query("noteDate") noteDate: String?,
        @Query("noteComment") noteComment: String?,
        @Query("workspace_id") workspaceId: String?
    ): Response<CreateFollowUpResponse>
}
