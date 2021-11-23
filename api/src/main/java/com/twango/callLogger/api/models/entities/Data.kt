package com.twango.callLogger.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "client_mobile")
    var clientMobile: String?,
    @Json(name = "client_name")
    var clientName: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "last_synced")
    var lastSynced: String?,
    @Json(name = "registered_on")
    var registeredOn: String?
)