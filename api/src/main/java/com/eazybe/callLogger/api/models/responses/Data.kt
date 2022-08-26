package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "lastSyncAt")
    var lastSyncAt: String?,
    @Json(name = "syncStartedAt")
    var syncStartedAt: String?,
    @Json(name = "sync_type")
    var syncType: String?,
    @Json(name = "updatedAt")
    var updatedAt: String?,
    @Json(name = "workspace_id")
    var workspaceId: Int?
)