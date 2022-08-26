package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleSignUpRequest(
    @Json(name = "gmailOTP")
    var gmailOTP: String?,
    @Json(name = "userEmail")
    var userEmail: String?,
    @Json(name = "registration_time")
    var syncTime: String?
)