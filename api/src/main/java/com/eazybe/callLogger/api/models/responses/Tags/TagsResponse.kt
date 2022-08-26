package com.eazybe.callLogger.api.models.responses.Tags


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsResponse(
    @Json(name = "data")
    var data: List<TagsData>?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "status")
    var status: Boolean?
)