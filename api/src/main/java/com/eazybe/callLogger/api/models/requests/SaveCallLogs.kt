package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveCallLogs(
    @Json(name = "call_duration")
    var callDuration: String?,
    @Json(name = "call_time")
    var callTime: String?,
    @Json(name = "call_type")
    var callType: Int?,
    @Json(name = "client_id")
    var clientId: Int?,
    @Json(name = "sync_datetime")
    var syncDatetime: String?,
    @Json(name = "user_mobile")
    var userMobile: String?,
    @Json(name = "user_name")
    var userName: String?
)