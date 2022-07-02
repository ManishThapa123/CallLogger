package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendOtpEmail(
    @Json(name = "userEmail")
    var userEmail: String?
)