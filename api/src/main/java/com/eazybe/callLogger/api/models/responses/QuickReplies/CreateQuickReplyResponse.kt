package com.eazybe.callLogger.api.models.responses.QuickReplies


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateQuickReplyResponse(
    @Json(name = "createdQuickReplyMessage")
    var createdQuickReplyMessage: CreatedQuickReplyMessage?,
    @Json(name = "status")
    var status: Boolean?
)