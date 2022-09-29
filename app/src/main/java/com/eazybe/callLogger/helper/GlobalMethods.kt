package com.eazybe.callLogger.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.amplifyframework.core.model.temporal.Temporal
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.eazybe.callLogger.BuildConfig
import com.eazybe.callLogger.R
import com.eazybe.callLogger.interfaces.ScreenshotInterface
import com.judemanutd.autostarter.AutoStartPermissionHelper
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
@Suppress("DEPRECATION")
object GlobalMethods {

    var ssInterface: ScreenshotInterface? = null

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

    fun saveContextForInterface(context: ScreenshotInterface) {
        ssInterface = context
    }

    fun getSSInterface(): ScreenshotInterface? {
        return ssInterface
    }

    fun hideKeyboard(context: Context, root: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(root.windowToken, 0)
    }

    fun showKeyboard(context: Context, root: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(root, InputMethodManager.SHOW_IMPLICIT)
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

        if (phoneNumberWithCountryCode.length < 10) {
            Toast.makeText(
                context,
                "Incorrect Number.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val url = if (phoneNumberWithCountryCode.contains("+"))
                "https://api.whatsapp.com/send?phone=" + "${phoneNumberWithCountryCode}&text=Hello"
            else
                "https://api.whatsapp.com/send?phone=" + "+91${phoneNumberWithCountryCode}&text=Hello"
            try {

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    this.data = Uri.parse(url)
                    this.`package` = "com.whatsapp"

                }

                try {
                    context.startActivity(intent)
                } catch (ex: ActivityNotFoundException) {

                }
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    context,
                    "Whatsapp is not installed in your phone.",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
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
            val intent = Intent(Intent.ACTION_DIAL, uri)
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

    fun convertFollowUpDate(date: Date): String {
        //"2022-8-11 13:04:05"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val convertedDate = dateFormat.format(date)
        return convertedDate
    }

    fun createDateToday(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 0)
        return convertMillisToDateAndTime("${calendar.timeInMillis}")
    }

    fun convertSyncedDateToMillis(date: Date): String {
        //"2022-8-11 13:04:05"
        //"2022-08-17T09:17:56.757Z
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        val convertedDate = dateFormat.parse(date)!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//
//        val updatedDate = date.dropLast(4)
//        val newDate = updatedDate+"000Z"

        val milliSeconds = date.time
//        val localDate =
//            LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
//
//        val milliSec = localDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return "$milliSeconds"
    }

    fun convertSyncedDateToMillisGetSync(date: String): String {
        //"2022-8-11 13:04:05"
//        val newDate = "$date.000Z"

        val localDate =
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

        val milliSec = localDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return "$milliSec"
    }

    fun convertDisplayUpDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy hh:mm aaa", Locale.getDefault())
        val convertedDate = dateFormat.format(date)
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
    fun calculateTimeDifference(date1: Long, date2: Long): Long {
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

    fun convertMillisToTempDate(millis: String): Temporal.DateTime {

        val date = Date(millis.toLong())
        val offsetMillis = TimeZone.getDefault().getOffset(date.time)
        val offsetSeconds = TimeUnit.MILLISECONDS.toSeconds(offsetMillis.toLong()).toInt()
        return Temporal.DateTime(date, offsetSeconds)

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

        return if (!dateFormat.isNullOrEmpty()) {
            var date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            try {
                date = formatter.parse(dateFormat)!!
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            Log.d("registeredDate", "Today is ${date.time}")
            "${date.time}"
        } else {
            val calendar: Calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, 0)
            "${calendar.timeInMillis}"
        }


    }

    fun copyTextToClipboard(textToCopy: String, context: Context) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "$textToCopy copied to clipboard", Toast.LENGTH_LONG).show()
    }

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = getApplication(context).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun sendQuickReplyTextToWhatsApp(title: String, textToSend: String, activity: Activity) {

        val data = HtmlCompat.fromHtml(
            "<p>$title</p>" +
                    "$textToSend <br>" +
                    "</p>", HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        try {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "image/*"
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, data.toString())
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            sendIntent.setPackage("com.whatsapp")
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(sendIntent, "Share via"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "WhatsApp Not Found.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
//                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
    }


    fun sendQuickReplyTextAndPDFToWhatsApp(
        title: String, textToSend: String, activity: Activity,
        fileAttachment: String, context: Context, fileName: String
    ) {
        val pdfFile = File(
            activity.getExternalFilesDir(null)?.absolutePath.toString(),
            "Eazybe Files/$fileName"
        )
        val uriFile = uriFromFile(context, pdfFile)
        val sendIntent = Intent(Intent.ACTION_SEND)
        try {

            sendIntent.type = "application/pdf"
            sendIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            sendIntent.putExtra(Intent.EXTRA_STREAM, uriFile)
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
            sendIntent.setPackage("com.whatsapp")
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(sendIntent, "Share via"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "WhatsApp Not Found.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    fun sendQuickReplyTextAndFileToWhatsApp(
        title: String, textToSend: String, activity: Activity,
        fileAttachment: String, context: Context, fileName: String
    ) {
        val file = File(
            activity.getExternalFilesDir(null)?.absolutePath.toString(),
            "Eazybe Files/$fileName"
        )
        val uriFile = uriFromFile(context, file)
        val sendIntent = Intent(Intent.ACTION_SEND)
        try {

            sendIntent.type = "*/*"
            sendIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            sendIntent.putExtra(Intent.EXTRA_STREAM, uriFile)
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
            sendIntent.setPackage("com.whatsapp")
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(sendIntent, "Share via"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "WhatsApp Not Found.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    fun sendQuickReplyTextAndImage(
        title: String, textToSend: String, activity: Activity,
        context: Context, fileAttachment: String, fileName: String, pbProgress: ProgressBar
    ) {


        CoroutineScope(Dispatchers.IO).launch {
            Glide.with(context).asBitmap().load(fileAttachment)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbProgress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        val sendIntent = Intent(Intent.ACTION_SEND)

                        try {
                            val bitmapURI = getLocalBitmapUri(resource!!, activity)
                            sendIntent.type = "image/*"
                            sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapURI)
                            sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
                            sendIntent.setPackage("com.whatsapp")
                            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            pbProgress.visibility = View.GONE
                            activity.startActivity(Intent.createChooser(sendIntent, "Share via"))
                        } catch (e: java.lang.Exception) {
                            try {
                                val bitmapURI = getLocalBitmapUri(resource!!, activity)
                                sendIntent.type = "image/*"
                                sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapURI)
                                sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
                                sendIntent.setPackage("com.whatsapp")
                                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                pbProgress.visibility = View.GONE
                                activity.startActivity(
                                    Intent.createChooser(
                                        sendIntent,
                                        "Share via"
                                    )
                                )
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(activity, "WhatsApp Not Found.", Toast.LENGTH_SHORT)
                                    .show()
                                pbProgress.visibility = View.GONE
                                e.printStackTrace()
                            }
                        }
                        return true
                    }

                }).submit()
        }


//        Picasso.get().load(fileAttachment).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//            .into(object : Target {
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
//                    val sendIntent = Intent(Intent.ACTION_SEND)
//
//                    try {
//                        val bitmapURI = getLocalBitmapUri(bitmap!!, activity)
//                        sendIntent.type = "image/*"
//                        sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapURI)
//                        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
//                        sendIntent.setPackage("com.whatsapp")
//                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        activity.startActivity(Intent.createChooser(sendIntent, "Share via"))
//                    } catch (e: java.lang.Exception) {
//                        Toast.makeText(activity, "WhatsApp Not Found.", Toast.LENGTH_SHORT).show()
//                        e.printStackTrace()
//                    }
//                }
//
//                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
//                Log.d("Failed","BitmapFailed")
//                }
//
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                    Log.d("Prepare","BitmapFailed")
//                }
//
//            })
    }

    fun checkIfFileExists(fileAttachment: String, context: Context): Boolean {
//        val extStorageDirectory = context.filesDir.absolutePath+"/" + "Eazybe Files" + "/" + fileAttachment
        val extStorageDirectory =
            context.getExternalFilesDir(null)?.absolutePath.toString() + "/" + "Eazybe Files/$fileAttachment"
        val file = File(extStorageDirectory)
        return file.exists()


    }

    fun writeResponseBodyToDisk(
        body: ResponseBody?,
        fileName: String,
        fileUrl: String,
        context: Context,
        activity: Activity
    ): Boolean {

        return try {

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

//            val extStorageDirectory = context.filesDir.absolutePath
            val extStorageDirectory = context.getExternalFilesDir(null)?.absolutePath.toString()


            val folder = File(extStorageDirectory, "Eazybe Files")

            if (!folder.exists() || !folder.isDirectory)
                folder.mkdirs()
//            val pathWhereToSaveFile = context.filesDir.absolutePath+fileName
//            val pathWhereToSaveFile = folder.path +"/" + fileName
            val pathWhereToSaveFile = File(extStorageDirectory, "Eazybe Files/$fileName")

//            val pathWhereToSaveFile = File(
//                context.getExternalFilesDir("Eazybe Files"),
//                fileName)

//            File(extStorageDirectory, "Eazybe Files/$fileName")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)
//                val fileSize = body?.contentLength()
//                var fileSizeDownloaded: Long = 0
                inputStream = body?.byteStream()
                outputStream = FileOutputStream(pathWhereToSaveFile)
                while (true) {
                    val read: Int = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break

                    }
                    outputStream.write(fileReader, 0, read)
//                    fileSizeDownloaded += read.toLong()
//                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()


            }
        } catch (e: IOException) {
            false
        }
    }

    private fun uriFromFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    private fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "EazybePicture" + System.currentTimeMillis() + ".JPG"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
            } else {
                Uri.fromFile(file)
            }
        } catch (e: IOException) {
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show()
        }
        return bmpUri
    }

}