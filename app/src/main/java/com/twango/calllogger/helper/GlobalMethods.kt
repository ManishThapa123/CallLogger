package com.twango.calllogger.helper

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.judemanutd.autostarter.AutoStartPermissionHelper
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
object GlobalMethods {

    /**
     * Make a standard toast that just contains text.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param message     The text to show.  Can be formatted text.
     *
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

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

//            builder.create().cancel()
            val success = AutoStartPermissionHelper.getInstance().getAutoStartPermission(context)
            Log.d("getAutoStartPermission",
                "$success")
            builder.create().dismiss()
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

    /**
     * In order to open a number in whats app, using the implicit Intent.
     * [phoneNumberWithCountryCode] is a required param.
     */
    fun openNumberInWhatsapp(phoneNumberWithCountryCode: String, context: Context) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumberWithCountryCode"
            context.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(context, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    /**
     * This function is called to send an Sms to the user, using the implicit Intent.
     * [phoneNumberWithCountryCode] is the user phone number required to send an SMS to.
     */
    fun sendSmsToUser(phoneNumberWithCountryCode: String, context: Context) {
        try {
            val uri = Uri.parse("smsto:$phoneNumberWithCountryCode")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "Hello,")
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Please try again..", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    /**
     * This function is called to make a call to the user, using the implicit Intent.
     * [phoneNumberWithCountryCode] is the user phone number to which the call has to be made.
     */
    fun callUser(phoneNumberWithCountryCode: String, context: Context) {
        try {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumberWithCountryCode")
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Couldn't place your call...", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    /**
     * In order to convert the milliseconds to Time Stamp using SimpleDateFormat.
     */
    fun convertMillisToDateAndTime(millis: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val convertedDate = dateFormat.format(millis.toLong())
        Log.d("dateInFormat", convertedDate)
        return convertedDate
    }

    fun convertMillisToDateAndTimeInMinutes(millis: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd  hh:mm a", Locale.getDefault())
        val convertedDate = dateFormat.format(millis.toLong())
        Log.d("dateInFormat", convertedDate)
        return convertedDate
    }

    /**
     * In order to convert the milliseconds to Hours, minutes format
     */
    fun convertMillisToTime(millis: String): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val convertedDate = dateFormat.format(millis.toLong())
        Log.d("timeInFormat", convertedDate)
        return convertedDate
    }

    /**
     * In order to convert the seconds to Hours, minutes and seconds format
     */
    fun convertSeconds(seconds: Int): String {
        val hour = seconds / 3600
        val minute = seconds % 3600 / 60
        val s = seconds % 60
        val sh = if (hour > 0) "$hour h" else ""
        val sm =
            (if (minute in 1..9 && hour > 0) "0" else "") + if (minute > 0) if (hour > 0 && s == 0) minute.toString() else "$minute min" else ""
        val ss =
            if (s == 0 && (hour > 0 || minute > 0)) "" else (if (s < 10 && (hour > 0 || minute > 0)) "0" else "") + s.toString() + " " + "sec"
        return sh + (if (hour > 0) " " else "") + sm + (if (minute > 0) " " else "") + ss
    }

    fun convertSecondsInHoursFormat(seconds: Int): String {
        return String.format("%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60)
    }

}