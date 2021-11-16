package com.twango.callLogger.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CallDetailsWithCount(
    @Json(name = "call_details")
    var callDetails: SampleEntity? = null,
    @Json(name = "count")
    var count: Int = 0,
)