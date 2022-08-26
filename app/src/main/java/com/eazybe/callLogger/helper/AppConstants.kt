package com.eazybe.callLogger.helper

import android.os.Looper

object AppConstants {
    const val AUTH_CREDENTIALS = "authCredentials"
    const val TIMESTAMP_FIRST = "timestampFirst"
    const val TIMESTAMP_LAST_SYNCED = "timestampLastSynced"
    const val LAST_SYNCED_TIME = "lastSyncedTime"
    const val EXPIRY_TIME = "expiryTime"
    const val EXPIRY_STATE = "expiryState"
    const val WHATSAPP_EXPIRY_TIME = "whatappExpiryTime"
    const val TIMESTAMP_SAVED = "timestampSaved"
    const val PERMISSION_STATE_SAVED = "permissionStateSaved"
    const val LOGIN_STATE_SAVED = "loginStateSaved"
    const val ORG_STATE_SAVED = "orgStateSaved"
    const val CALL_LOG_STATE_SAVED = "callLogStateSaved"
    const val CALL_SYNC_STATE = "syncState"
    const val CLIENT_REGISTRATION_DATA_SAVED = "clientRegistrationDataSaved"
    const val CLIENT_REGISTRATION_DATA_EMAIL = "clientRegistrationDataSavedEmail"
    const val AUTO_START_PERMISSION_STATE_SAVED = "autoStartPermissionStateSaved"
    const val SIM_SUBSCRIPTION_ID_SAVED = "simSubscriptionIdSaved"
    const val SIM_SUBSCRIPTION_ID_SUB_SAVED = "simSubscriptionIdSubSaved"
    const val SIM_SUBSCRIPTION_ICC_ID_SAVED = "simSubscriptionIccIdSaved"


    const val SHIFT_OFF = 0
    const val SHIFT_ON_ONE_CHAR = 1
    const val SHIFT_ON_PERMANENT = 2

    // limit the count of alternative characters that show up at long pressing a key
    const val MAX_KEYS_PER_MINI_ROW = 9

    // shared prefs
    const val VIBRATE_ON_KEYPRESS = "vibrate_on_keypress"
    const val SHOW_POPUP_ON_KEYPRESS = "show_popup_on_keypress"
    const val LAST_EXPORTED_CLIPS_FOLDER = "last_exported_clips_folder"
    const val KEYBOARD_LANGUAGE = "keyboard_language"

    // differentiate current and pinned clips at the keyboards' Clipboard section
    const val ITEM_SECTION_LABEL = 0
    const val ITEM_CLIP = 1

    const val LANGUAGE_ENGLISH_QWERTY = 0
    const val LANGUAGE_RUSSIAN = 1
    const val LANGUAGE_FRENCH = 2
    const val LANGUAGE_ENGLISH_QWERTZ = 3
    const val LANGUAGE_SPANISH = 4
    const val LANGUAGE_GERMAN = 5

    const val SHORT_ANIMATION_DURATION = 150L

    fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()
}