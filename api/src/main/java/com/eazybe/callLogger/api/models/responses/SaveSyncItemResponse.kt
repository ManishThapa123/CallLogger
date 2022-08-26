package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveSyncItemResponse(
    @Json(name = "data")
    var `data`: List<Data>?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "type")
    var type: Boolean?
)