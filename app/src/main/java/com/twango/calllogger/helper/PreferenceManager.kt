package com.twango.calllogger.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager constructor(private val context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CallLoggerTwango", 0)
    private var spEditor: SharedPreferences.Editor = sharedPreferences.edit()

    //Save it after calling the api.
    fun saveFirstTimeRegisterMillis(timeInMillis: String) {
        spEditor.putString(AppConstants.TIMESTAMP_FIRST, timeInMillis)
        spEditor.putBoolean(AppConstants.TIMESTAMP_SAVED, true)
        spEditor.commit()
    }

    fun getFirstTimeRegisterMillis(): String? {
        return sharedPreferences.getString(AppConstants.TIMESTAMP_FIRST, null)
    }

    /**
     * to check if the user is already logged in
     */
    fun isSavedFirstRegisterTimeStamp(): Boolean {
        return sharedPreferences.getBoolean(AppConstants.TIMESTAMP_SAVED, false)
    }

    fun saveLastSyncedTimeInMillis(timeInMillis: String) {
        spEditor.putString(AppConstants.TIMESTAMP_LAST_SYNCED, timeInMillis)
        spEditor.commit()
    }

    fun getLastSyncedTimeInMillis(): String? {
        return sharedPreferences.getString(AppConstants.TIMESTAMP_LAST_SYNCED, null)
    }

    fun savePermissionState(savePermission: Boolean) {
        spEditor.putBoolean(AppConstants.PERMISSION_STATE_SAVED, true)
        spEditor.commit()
    }

    fun getPermissionState(): Boolean {
        return sharedPreferences.getBoolean(AppConstants.PERMISSION_STATE_SAVED, false)
    }

    fun saveAutoStartPermissionStart() {
        spEditor.putBoolean(AppConstants.AUTO_START_PERMISSION_STATE_SAVED, true)
        spEditor.commit()
    }

    fun getSaveAutoStartPermissionStart(): Boolean {
        return sharedPreferences.getBoolean(AppConstants.AUTO_START_PERMISSION_STATE_SAVED, false)
    }

    fun saveLoginState(saveLoginState: Boolean) {
        spEditor.putBoolean(AppConstants.LOGIN_STATE_SAVED, true)
        spEditor.commit()
    }

    fun getLoginState(): Boolean {
        return sharedPreferences.getBoolean(AppConstants.LOGIN_STATE_SAVED, false)
    }

    //to save data after registration/login
    fun saveClientRegistrationData(status: String) {
        spEditor.putString(AppConstants.CLIENT_REGISTRATION_DATA_SAVED, status)
        spEditor.commit()
    }

    //to get auth credentials
    fun getClientRegistrationData(): String? {
        return sharedPreferences.getString(AppConstants.CLIENT_REGISTRATION_DATA_SAVED, null)
    }

}