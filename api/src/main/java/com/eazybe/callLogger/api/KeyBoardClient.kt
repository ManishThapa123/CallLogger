package com.eazybe.callLogger.api

import com.eazybe.callLogger.api.models.responses.AllCustomerFollowUpResponse
import com.eazybe.callLogger.api.models.responses.CreateFollowUpResponse
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class KeyBoardClient : KeyBoardApiInterface {
    val API_PREFIX = "https://eazybe.com/api/v1/whatzapp/"

    /**
     * The Retrofit class generates an implementation of the [ApiInterface] interface.
     */
    // Set the custom client when building adapter
    private val okHttpBuilder = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(CallLoggerClient.API_PREFIX)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpBuilder)
        .build()
    var service: CallLoggerAPIInterface = retrofit.create(CallLoggerAPIInterface::class.java)
    var serviceKeyBoard: KeyBoardApiInterface = retrofit.create(KeyBoardApiInterface::class.java)


    override suspend fun getAllCustomerFollowups(userMobile: String?): Response<AllCustomerFollowUpResponse> {
        return serviceKeyBoard.getAllCustomerFollowups(userMobile)
    }

    override suspend fun createFollowUp(
        userMobile: String?,
        customerMobile: String?,
        chatId: String?,
        name: String?,
        priority: String?,
        followUpDate: String?,
        noteDate: String?,
        noteComment: String?,
        workspaceId: String?
    ): Response<CreateFollowUpResponse> {
        return serviceKeyBoard.createFollowUp(
            userMobile,
            customerMobile,
            chatId,
            name,
            priority,
            followUpDate,
            noteDate,
            noteComment,
            workspaceId)
    }
}