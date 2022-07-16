package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataFollowUp(
    @Json(name = "chat_id")
    var chatId: String?,
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "customer_email")
    var customerEmail: String?,
    @Json(name = "customer_mobile")
    var customerMobile: String?,
    @Json(name = "follow_up_date")
    var followUpDate: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "name")
    var name: String?,
    @Json(name = "notes")
    var notes: List<Note>?,
    @Json(name = "priority")
    var priority: String?,
    @Json(name = "reminder_id")
    var reminderId: Int?,
    @Json(name = "updatedAt")
    var updatedAt: String?,
    @Json(name = "user_mobile")
    var userMobile: String?,
    @Json(name = "userToCustomerId")
    var userToCustomerId: Any?,
    @Json(name = "value")
    var value: Any?,
    @Json(name = "workspace_id")
    var workspaceId: Any?
)