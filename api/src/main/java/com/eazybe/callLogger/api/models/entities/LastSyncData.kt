package com.eazybe.callLogger.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastSyncData(
    @Json(name = "client_mobile")
    var clientMobile: String? = "",
    @Json(name = "client_name")
    var clientName: String? = "",
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "last_synced")
    var lastSynced: String? = "",
    @Json(name = "registered_on")
    var registeredOn: String? = ""
)