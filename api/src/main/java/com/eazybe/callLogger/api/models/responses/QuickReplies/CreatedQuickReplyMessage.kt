package com.eazybe.callLogger.api.models.responses.QuickReplies


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatedQuickReplyMessage(
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "file_name")
    var fileName: String?,
    @Json(name = "fileUrl")
    var fileUrl: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "quick_reply_message")
    var quickReplyMessage: String?,
    @Json(name = "quickReplyTitle")
    var quickReplyTitle: String?,
    @Json(name = "updatedAt")
    var updatedAt: String?,
    @Json(name = "workspace_id")
    var workspaceId: String?
)