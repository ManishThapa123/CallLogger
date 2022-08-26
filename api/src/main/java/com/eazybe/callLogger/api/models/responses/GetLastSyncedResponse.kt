package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLastSyncedResponse(
    @Json(name = "sync_data")
    var syncData: List<SyncData>?,
    @Json(name = "type")
    var type: Boolean?
)