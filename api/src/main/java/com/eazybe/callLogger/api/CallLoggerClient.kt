package com.eazybe.callLogger.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object CallLoggerClient {

//    val API_PREFIX = "https://partner.hansmatrimony.com/api/"
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
        .baseUrl(API_PREFIX)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpBuilder)
        .build()
    var service: CallLoggerAPIInterface = retrofit.create(CallLoggerAPIInterface::class.java)
}