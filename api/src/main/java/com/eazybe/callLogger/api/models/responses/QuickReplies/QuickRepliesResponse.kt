package com.eazybe.callLogger.api.models.responses.QuickReplies


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuickRepliesResponse(
    @Json(name = "orgDetails")
    var orgDetails: List<Any>?,
    @Json(name = "orgDetected")
    var orgDetected: Boolean?,
    @Json(name = "status")
    var status: Boolean?,
    @Json(name = "userQuickRepliesMessages")
    var userQuickRepliesMessages: List<UserQuickRepliesMessage>?
)