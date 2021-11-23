package com.twango.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.twango.callLogger.api.models.entities.Data
import com.twango.callLogger.api.models.entities.LastSyncData

@JsonClass(generateAdapter = true)
data class LastSyncResponse(
    @Json(name = "data")
    var `data`: Data?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "type")
    var type: Boolean?
)