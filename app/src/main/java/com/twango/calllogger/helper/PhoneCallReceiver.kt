package com.twango.calllogger.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.twango.calllogger.helper.GlobalMethods.showToast
import java.util.*

  class PhoneCallReceiver: BroadcastReceiver() {

    companion object {
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private lateinit var callStartTime: Date
        private var isIncoming: Boolean = false
        private lateinit var savedNumber: String

        private const val ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL"
        private const val PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER"
    }
      @Suppress("DEPRECATION")
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
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
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

//    //Derived classes should override these to respond to specific events of interest
//    abstract fun onIncomingCallStarted(ctx: Context, number: String, start: Date)
//    abstract fun onOutgoingCallStarted(ctx: Context, number: String, start: Date)
//    abstract fun onIncomingCallEnded(ctx: Context, number: String, start: Date, end: Date)
//    abstract fun onOutgoingCallEnded(ctx: Context, number: String, start: Date, end: Date)
//    abstract fun onMissedCall(ctx: Context, number: String, start: Date)

    private fun onCallStateChanged(context: Context, state: Int) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
//                savedNumber = number
//                onIncomingCallStarted(context, number, callStartTime)
                showToast(context,"Call Ringing")
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->{
                //Transition of ringing->offhook are pickups of incoming calls. Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
//                    onOutgoingCallStarted(context, savedNumber, callStartTime)
                }
                showToast(context,"Call Started")
            }

            TelephonyManager.CALL_STATE_IDLE ->{
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                when {
                    lastState == TelephonyManager.CALL_STATE_RINGING -> {
                        //Ring but no pickup-  a miss
//                        onMissedCall(context, savedNumber, callStartTime)
                    }
                    isIncoming -> {
//                        onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                    }
                    else -> {
//                        onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                    }

                }
                showToast(context,"Call Ended")
            }
        }
        lastState = state
    }
}