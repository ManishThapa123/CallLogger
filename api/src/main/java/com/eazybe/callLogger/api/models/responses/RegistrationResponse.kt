package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.eazybe.callLogger.api.models.entities.Data

@JsonClass(generateAdapter = true)
data class RegistrationResponse(
    @Json(name = "data")
    var `data`: Data?,
    @Json(name = "message")
    var message: String?,
    @Json(name = "type")
    var type: Boolean?
)