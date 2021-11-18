package com.twango.calllogger.helper

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.MainActivity
import com.twango.calllogger.R
import com.twango.calllogger.helper.CallLogsUpdatingManager.updateExistingCallLogs
import com.twango.calllogger.repository.BaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhoneCallReceiver : BroadcastReceiver() {
    @Inject
    lateinit var callLogsHelper: CallLogsHelper

    @Inject
    lateinit var baseRepository: BaseRepository

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private lateinit var callStartTime: Date
        private var isIncoming: Boolean = false
        private lateinit var savedNumber: String

        private const val ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL"
        private const val PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        // We listen to two intents.
        // The new outgoing call only tells us of an outgoing call.
        // We use it to get the number.
        if (intent?.action == ACTION_NEW_OUTGOING_CALL) {
            if (intent.extras != null) {
                savedNumber = intent.extras!!.getString(PHONE_NUMBER)!!
            }
        } else {
            val stateStr = intent?.extras!!.getString(TelephonyManager.EXTRA_STATE)
            var state = 0
            when (stateStr) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    state = TelephonyManager.CALL_STATE_IDLE
                    onCallStateChanged(context!!, state)
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    state = TelephonyManager.CALL_STATE_OFFHOOK
                    onCallStateChanged(context!!, state)
                }
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    state = TelephonyManager.CALL_STATE_RINGING
                    onCallStateChanged(context!!, state)
                }
            }
        }
    }

    private fun onCallStateChanged(context: Context, state: Int) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
//                showToast(context, "Call Ringing")
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                //Transition of ringing-> offhook are pickups of incoming calls. Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                }
//                showToast(context, "Call Started")
            }

            TelephonyManager.CALL_STATE_IDLE -> {
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                when {
                    lastState == TelephonyManager.CALL_STATE_RINGING -> {
                        //Ring but no pickup-  a miss
                        callLogsHelper.getLatestCallLog { callDetails ->
                            Log.d("latestMissedCall", "$callDetails")

                            updateLatestCallLog(
                                "+919470105448",
                                if (callDetails.userName.isNullOrEmpty())
                                    "Unknown"
                                else
                                    callDetails.userName!!,
                                callDetails.userNumber!!,
                                callDetails.time!!,
                                callDetails.callDuration!!,
                                callDetails.callType!!,
                                callLogsHelper.createDate(0),
                                context
                            )
                        }

                    }
                    isIncoming -> {
                        //this means call was incoming and picked up and ended.
                        callLogsHelper.getLatestCallLog { callDetails ->
                            Log.d("latestIncomingCall", "$callDetails")
                            updateLatestCallLog(
                                "+919470105448",
                                if (callDetails.userName.isNullOrEmpty())
                                    "Unknown"
                                else
                                    callDetails.userName!!,
                                callDetails.userNumber!!,
                                callDetails.time!!,
                                callDetails.callDuration!!,
                                callDetails.callType!!,
                                callLogsHelper.createDate(0),
                                context
                            )

                        }
                    }
                    else -> {
                        //this means call was outgoing and picked up and ended.
                        callLogsHelper.getLatestCallLog { callDetails ->
                            Log.d("latestOutgoingCall", "$callDetails")
                            updateLatestCallLog(
                                "+919470105448",
                                if (callDetails.userName.isNullOrEmpty())
                                    "Unknown"
                                else
                                    callDetails.userName!!,
                                callDetails.userNumber!!,
                                callDetails.time!!,
                                callDetails.callDuration!!,
                                callDetails.callType!!,
                                callLogsHelper.createDate(0),
                                context
                            )
                        }
                    }

                }
//                showToast(context, "Call Ended")
            }
        }
        lastState = state
    }
    //on end of call Receive the last call from call logs,
    // upload to api to server and show the notification for call synced.

    private fun updateLatestCallLog(
        registeredNumber: String,
        userName: String, userNumber: String, callTime: String,
        callDuration: String, callType: String,
        syncDateTime: String,
        context: Context
    ) = scope.launch {
        baseRepository.updateClientCallLog(
            registeredNumber,
            userName,
            userNumber,
            GlobalMethods.convertMillisToDateAndTime(callTime),
            GlobalMethods.convertSecondsInHoursFormat(callDuration.toInt()),
            callType,
            GlobalMethods.convertMillisToDateAndTime(syncDateTime)
        ).let { response ->
            if (response.isSuccessful) {
                if (response.body()?.type == true) {
                    Log.d("Response", "Success, call notification")
                    Log.d("CallType", callType)
                    sendNotification(context, registeredNumber, syncDateTime)

                    //Save the last syncdate.
                    preferenceManager.saveLastSyncedTimeInMillis(syncDateTime)
                    //We have to put a check to see whether the app is on foreground or not.
                    val isAppRunning = isAppInforegrounded()
                    Log.d("isAppRunning", "$isAppRunning")
                    if (isAppRunning) {
                        updateExistingCallLogs(
                            callType,
                            "testing",
                            SampleEntity(userName, userNumber, callTime, callDuration, callType)
                        )
                    }

                } else
                    Log.d("Response", "Success, Failed to update in the backend")
            } else {
                Log.d("Response", "Failure, Alert user")
            }
        }
    }

    private fun sendNotification(
        context: Context,
        registeredNumber: String?,
        syncDateTime: String
    ) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val channelId = "Call Logger"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.call_icon)
            .setContentTitle("Call Synced")
            .setContentText("Your Last call has been synced at $syncDateTime")
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 100, 200, 300, 400, 300, 400))

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Call Logger Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Channel for Call Logger notifications"
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            101 /* ID of notification */,
            notificationBuilder.build()
        )

    }

    fun isAppInforegrounded(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }
}