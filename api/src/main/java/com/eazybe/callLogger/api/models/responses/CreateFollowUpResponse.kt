package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateFollowUpResponse(
    @Json(name = "data")
    var `data`: DataFollowUpCreated?,
    @Json(name = "status")
    var status: Boolean?
)