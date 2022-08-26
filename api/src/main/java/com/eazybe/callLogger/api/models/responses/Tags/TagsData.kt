package com.eazybe.callLogger.api.models.responses.Tags


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsData(
    @Json(name = "column_name")
    var columnName: String?,
    @Json(name = "field_id")
    var fieldId: Int?,
    @Json(name = "field_value")
    var fieldValue: String?,
    @Json(name = "is_default")
    var isDefault: Int?,
    @Json(name = "is_editable")
    var isEditable: Int?,
    @Json(name = "is_tag")
    var isTag: Int?,
    @Json(name = "tag_id")
    var tagId: Int?
)