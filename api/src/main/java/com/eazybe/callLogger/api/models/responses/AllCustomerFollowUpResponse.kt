package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllCustomerFollowUpResponse(
    @Json(name = "data")
    var `data`: List<DataFollowUp>?
)