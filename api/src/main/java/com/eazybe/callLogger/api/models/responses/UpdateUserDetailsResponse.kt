package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateUserDetailsResponse(
    @Json(name = "data")
    var `data`: DataX?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "profile_pic")
    var profilePic: String?,
    @Json(name = "type")
    var type: Boolean?
)