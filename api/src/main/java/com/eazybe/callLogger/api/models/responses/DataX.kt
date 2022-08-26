package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataX(
    @Json(name = "name")
    var name: String?,
    @Json(name = "workspace_id")
    var workspaceId: String?
)