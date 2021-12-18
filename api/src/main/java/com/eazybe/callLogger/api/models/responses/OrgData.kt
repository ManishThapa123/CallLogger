package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrgData(
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "org_address")
    var orgAddress: String?,
    @Json(name = "org_code")
    var orgCode: String?,
    @Json(name = "org_logo")
    var orgLogo: String?,
    @Json(name = "org_name")
    var orgName: String?,
    @Json(name = "org_phone")
    var orgPhone: Long?,
    @Json(name = "updatedAt")
    var updatedAt: String?
)