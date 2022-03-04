package com.eazybe.callLogger.helper

import a.a.b.a.c.l.e.c
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import com.judemanutd.autostarter.AutoStartPermissionHelper
import com.eazybe.callLogger.R
import com.eazybe.callLogger.container.CallLoggerApplication
import dagger.hilt.android.internal.Contexts.getApplication
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
            Log.d(
                "getAutoStartPermission",
                "$success"
            )
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
            val uri = Uri.parse("tel:$phoneNumberWithCountryCode")
            val intent = Intent(Intent.ACTION_DIAL,uri)
            context.startActivity(intent)
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

    /**
     * In order to convert the milliseconds to Time Stamp using SimpleDateFormat.
     */
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
     * In order to calculate the time difference using millis
     */
    fun calculateTimeDifference(date1: Long, date2: Long): Long{
        val diff = date2 - date1
        val seconds = diff / 1000
        return seconds
    }
    /**
     * In order to convert the milliseconds to Hours, minutes format
     */
    fun convertMillisToDate(millis: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val convertedDate = dateFormat.format(millis.toLong())
        Log.d("dateInFormat", convertedDate)
        return convertedDate
    }

    /**
     * In order to convert the seconds to Hours, minutes and seconds format with text included.
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

    /**
     * In order to convert the seconds to Hours, minutes and seconds format
     */
    fun convertSecondsInHoursFormat(seconds: Int): String {
        return String.format("%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60)
    }

    /**
     * Make an animated Motion toast that contains text, title and toast type.
     *
     * @param activity  The activity to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param message     The text to show.  Can be formatted text.
     * @param title     The title to show.  Can be formatted text.
     * @param type     The type of motion toast.
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     *
     */
    fun showMotionToast(
        activity: Activity,
        title: String,
        message: String,
        type: String,
        context: Context
    ) {
        when (type) {
            "success" -> {
                MotionToast.darkColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.poppins)
                )
            }
            "failure" -> {
                MotionToast.darkColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.poppins)
                )
            }
            "warning" -> {
                MotionToast.darkColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.poppins)
                )
            }
            "no_internet" ->
                MotionToast.darkColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.poppins)
                )
            "info" ->
                MotionToast.darkColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.poppins)
                )
        }

    }

    /**
     * In order to get the milliseconds using the simpleDateFormat.
     */

    fun getMilliFromDate(dateFormat: String?): String {
        var date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat!!)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("registeredDate", "Today is ${date.time}")
        return "${date.time}"
    }

     fun copyTextToClipboard(textToCopy: String, context: Context) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "$textToCopy copied to clipboard", Toast.LENGTH_LONG).show()
    }

     fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = getApplication(context).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}