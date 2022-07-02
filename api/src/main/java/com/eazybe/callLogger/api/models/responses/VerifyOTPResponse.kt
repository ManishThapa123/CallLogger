package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyOTPResponse(
    @Json(name = "OtpVerification")
    var otpVerification: Boolean?,
    @Json(name = "status")
    var status: Boolean?,
    @Json(name = "workspace_details")
    var workspaceDetails: WorkspaceDetails?,
    @Json(name = "workspace_data")
    var workspaceData: WorkspaceDetails?
)