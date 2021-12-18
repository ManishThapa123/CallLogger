package com.eazybe.callLogger.api.models.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateOrgRequest(
    @Json(name = "org_id")
    var orgId: Int?,
    @Json(name = "user_id")
    var userId: Int?
)