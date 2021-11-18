package com.twango.callLogger.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object CallLoggerClient {

    val API_PREFIX = "https://partner.hansmatrimony.com/api/"


    /**
     * The Retrofit class generates an implementation of the [ApiInterface] interface.
     */
    // Set the custom client when building adapter
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_PREFIX)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    var service: CallLoggerAPIInterface = retrofit.create(CallLoggerAPIInterface::class.java)
}