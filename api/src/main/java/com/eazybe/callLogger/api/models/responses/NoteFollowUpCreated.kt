package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NoteFollowUpCreated(
    @Json(name = "date")
    var date: String?,
    @Json(name = "note")
    var note: String?
)