package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterData(
    @Json(name = "active_status")
    var activeStatus: Int?,
    @Json(name = "client_mobile")
    var clientMobile: Long?,
    @Json(name = "client_name")
    var clientName: String?,
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "last_synced")
    var lastSynced: String?,
    @Json(name = "org_id")
    var orgId: Int?,
    @Json(name = "updatedAt")
    var updatedAt: String?,
    @Json(name = "logging_starts_from")
    var registrationDate: String?
)