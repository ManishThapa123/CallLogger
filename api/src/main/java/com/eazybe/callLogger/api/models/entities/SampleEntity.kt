package com.eazybe.callLogger.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SampleEntity(
    @Json(name = "user_name")
    var userName: String? = "Unknown",
    @Json(name = "user_number")
    var userNumber: String? = null,
    @Json(name = "call_time")
    var time: String? = null,
    @Json(name = "call_duration")
    var callDuration: String? = null,
    @Json(name = "call_type")
    var callType: String? = null,
    @Json(name = "subscribed_sim_Id")
    var subscribedSimID: String? = null,
    @Json(name = "call_Log_Id")
    var callLogId: String? = null
)