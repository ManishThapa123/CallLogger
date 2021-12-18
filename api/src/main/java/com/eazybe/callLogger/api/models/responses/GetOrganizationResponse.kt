package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetOrganizationResponse(
    @Json(name = "data")
    var orgData: List<OrgData>?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "type")
    var type: Boolean?
)