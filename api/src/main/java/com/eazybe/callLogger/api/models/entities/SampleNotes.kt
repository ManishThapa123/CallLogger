package com.eazybe.callLogger.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SampleNotes(
    @Json(name = "notes")
    var notes: String? = null,
    @Json(name = "date")
    var dateTime: String? = null
)