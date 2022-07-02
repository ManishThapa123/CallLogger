package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatusResponse(
    @Json(name = "message")
    var message: String?,
    @Json(name = "status")
    var status: Boolean?
)