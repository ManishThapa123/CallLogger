package com.eazybe.callLogger.api.models.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkspaceDetails(
    @Json(name = "active_status")
    var activeStatus: Int?,
    @Json(name = "callyzer_mobile")
    var callyzerMobile: Any?,
    @Json(name = "country_code")
    var countryCode: String?,
    @Json(name = "createdAt")
    var createdAt: String?,
    @Json(name = "email")
    var email: String?,
    @Json(name = "email_send")
    var emailSend: Int?,
    @Json(name = "email_send_at")
    var emailSendAt: String?,
    @Json(name = "email_verified_at")
    var emailVerifiedAt: String?,
    @Json(name = "google_calender_linked")
    var googleCalenderLinked: Any?,
    @Json(name = "google_calender_token")
    var googleCalenderToken: Any?,
    @Json(name = "id")
    var id: Int?,
    @Json(name = "industry_type")
    var industryType: Int?,
    @Json(name = "is_refered_done")
    var isReferedDone: Int?,
    @Json(name = "is_review_done")
    var isReviewDone: Int?,
    @Json(name = "last_synced")
    var lastSynced: String?,
    @Json(name = "logging_starts_from")
    var loggingStartsFrom: String?,
    @Json(name = "mobile")
    var mobile: Any?,
    @Json(name = "mute_notification")
    var muteNotification: Int?,
    @Json(name = "name")
    var name: Any?,
    @Json(name = "org_id")
    var orgId: Any?,
    @Json(name = "otp")
    var otp: Any?,
    @Json(name = "parent_id")
    var parentId: Any?,
    @Json(name = "sorting_labels")
    var sortingLabels: Any?,
    @Json(name = "uninstallStatus")
    var uninstallStatus: String?,
    @Json(name = "updatedAt")
    var updatedAt: String?,
    @Json(name = "user_system_labels")
    var userSystemLabels: String?,
    @Json(name = "user_type")
    var userType: Int?
)