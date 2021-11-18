package com.twango.callLogger.api.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveCallLogsResponse(
    @Json(name = "type")
    var type: Boolean,
    @Json(name = "message")
    var message: String
)