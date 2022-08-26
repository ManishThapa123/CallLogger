package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaveSyncCallsRequestItem(
    @Json(name = "sync_type")
    var syncType: String?,
    @Json(name = "workspace_id")
    var workspaceId: Int?,
    @Json(name = "sync_time")
    var syncTime: String?
)