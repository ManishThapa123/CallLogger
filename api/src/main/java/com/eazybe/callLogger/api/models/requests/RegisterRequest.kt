package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "active_status")
    var activeStatus: Int?,
    @Json(name = "client_mobile")
    var clientMobile: String?,
    @Json(name = "client_name")
    var clientName: String?,
    @Json(name = "last_synced")
    var lastSynced: String?,
    @Json(name = "org_id")
    var orgId: Int?
)