package com.twango.calllogger.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.judemanutd.autostarter.AutoStartPermissionHelper

@SuppressLint("StaticFieldLeak")
object GlobalMethods {
    /**
     * In order to check if the device is supported by the library.
     * If false, the method will return true as long as the permission exist,
     * even if the screen is not supported by the library.
     */
    fun checkIfAutoStartPermissionAvailable(context: Context): Boolean {
        val autoStartAvailable =
            AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(context)
        Log.d("checkAutoStartPermission", "$autoStartAvailable")
        return autoStartAvailable
    }

    /**
     * To open the settings for auto start permission.
     * If true is passed as the second parameter,
     * it will attempt to open the activity, otherwise it will just check its existence.
     */
    fun getAutoStartPermission(context: Context) {
        //PLEASE READ BEFORE MOVING FORWARD.
        //Note: You cannot check whether autorun permission is enabled or not because autorun
        //feature is never provided by android os like mi, vivo, oppo, letv etc.
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enable AutoStart")
        builder.setMessage("Please allow auto start to run background sync process.")
        builder.setPositiveButton("Allow") { _, _ ->
            val success = AutoStartPermissionHelper.getInstance().getAutoStartPermission(context)
            Log.d("getAutoStartPermission", "$success")
        }
        builder.setCancelable(false)
        builder.create()
        builder.show()
    }

    /**
     * In order to log the device information.
     */
    fun logDeviceInfo() {
        val tag = "DeviceInfo"
        Log.w(tag, "Board: ${Build.BOARD}")
        Log.w(tag, "Brand: ${Build.BRAND}")
        Log.w(tag, "Device: ${Build.DEVICE}")
        Log.w(tag, "Display: ${Build.DISPLAY}")
        Log.w(tag, "Hardware: ${Build.HARDWARE}")
        Log.w(tag, "Manufacturer: ${Build.MANUFACTURER}")
        Log.w(tag, "Product: ${Build.PRODUCT}")
        Log.w(tag, "Version.Release: ${Build.VERSION.RELEASE}")
        Log.w(tag, "Version.Codename: ${Build.VERSION.CODENAME}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.w(tag, "Version.BaseOS: ${Build.VERSION.BASE_OS}")
            Log.w(tag, "Version.SecurityPatch: ${Build.VERSION.SECURITY_PATCH}")
        }
    }
}