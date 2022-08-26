package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncData(
    @Json(name = "id")
    var id: Int?,
    @Json(name = "last_sync_date")
    var lastSyncAt: String?,
    @Json(name = "sync_started_at")
    var syncStartedAt: String?,
    @Json(name = "sync_type")
    var syncType: String?,
    @Json(name = "is_active")
    var isActive: String?,
    @Json(name = "vailidity")
    var vailidity: String?
)