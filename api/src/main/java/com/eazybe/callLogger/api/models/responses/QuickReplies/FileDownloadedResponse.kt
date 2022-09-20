package com.eazybe.callLogger.api.models.responses.QuickReplies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.Response

@JsonClass(generateAdapter = true)
data class FileDownloadedResponse (
    @Json(name = "response")
    var response: Response<ResponseBody>,
    @Json(name = "fileName")
    var fileName: String?,
    @Json(name = "fileURL")
    var fileURL: String?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "message")
    var message: String?
    )