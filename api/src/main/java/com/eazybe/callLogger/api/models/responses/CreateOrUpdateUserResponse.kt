package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateOrUpdateUserResponse(
    @Json(name = "data")
    var data: UserData?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "new_sync_time")
    var newSyncTime: String?,
    @Json(name = "type")
    var type: Boolean?
)