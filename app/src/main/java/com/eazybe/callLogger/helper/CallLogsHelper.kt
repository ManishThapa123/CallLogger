package com.eazybe.callLogger.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.provider.CallLog
import android.telecom.TelecomManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.eazybe.callLogger.api.models.entities.CallDetailsWithCount
import com.eazybe.callLogger.api.models.entities.SampleEntity
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CallLogsHelper @Inject constructor(
    private val context: Context,
    private val preferenceManager: PreferenceManager) {

    private var allCallLogsList: ArrayList<SampleEntity>? = null
    private var missedCallList: ArrayList<SampleEntity>? = null
    private var outGoingCallList: ArrayList<SampleEntity>? = null
    private var incomingCallList: ArrayList<SampleEntity>? = null
    private var rejectedCallList: ArrayList<SampleEntity>? = null
    private var fromDate: String? = null
    private var neverAttendedList: ArrayList<SampleEntity>? = null
    private var neverPickedUpList: ArrayList<SampleEntity>? = null
    private var lastLogCount = false

    fun deleteCallById(callLogId: String, context: Context) {
    }

    private fun loadCallLogs() {
        allCallLogsList = ArrayList()
        missedCallList = ArrayList()
        outGoingCallList = ArrayList()
        incomingCallList = ArrayList()
        rejectedCallList = ArrayList()

        val numberCol = CallLog.Calls.NUMBER
        val durationCol = CallLog.Calls.DURATION
        val typeCol = CallLog.Calls.TYPE // 1 - Incoming, 2 - Outgoing, 3 - Missed , 5- Rejected
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val subscribedSimIDCol = CallLog.Calls.PHONE_ACCOUNT_ID
        val callLogIdCol = CallLog.Calls._ID
        val projection =
            arrayOf(
                numberCol,
                durationCol,
                typeCol,
                nameCol,
                dateCol,
                subscribedSimIDCol,
                callLogIdCol
            )
        val mSelectionClause = CallLog.Calls.DATE + " BETWEEN ? AND ?"

        /**
         * In order to check whether the firstTimeRegistration date has been saved in the preference or not
         */
        val preferenceFirstTimeRegisteredDate = preferenceManager.isSavedFirstRegisterTimeStamp()
        Log.d("FirstTimeDate", "$preferenceFirstTimeRegisteredDate")
        //When saved use the same date, else create the current date or call an api in this case.
        if (preferenceFirstTimeRegisteredDate) {
            fromDate = preferenceManager.getFirstTimeRegisterMillis()
        } else {
//            fromDate = "1636455990849"
            fromDate = createDate(0)
            preferenceManager.saveFirstTimeRegisterMillis(fromDate!!)
            Log.d("fromDate2", fromDate!!)
        }

        //from date will be the date of registration, the user has signed up.
        val mSelectionArgs = arrayOf(fromDate, createDate(1))
        GlobalMethods.convertMillisToDateAndTime(fromDate!!)
        mSelectionArgs.forEach {
            Log.d("SelectionDate:", it!!)
        }

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, mSelectionClause, mSelectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER)
        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val durationColIdx = cursor.getColumnIndex(durationCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)
        val nameColIdx = cursor.getColumnIndex(nameCol)
        val dateColIdx = cursor.getColumnIndex(dateCol)
        val subscribedSimIDColIdx = cursor.getColumnIndex(subscribedSimIDCol)
        val callLogIdColIdx = cursor.getColumnIndex(callLogIdCol)

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val duration = cursor.getString(durationColIdx)
            val type = cursor.getString(typeColIdx)
            val name = cursor.getString(nameColIdx)
            val date = cursor.getString(dateColIdx)
            val subscribedSimID = cursor.getString(subscribedSimIDColIdx)
            val callLogId = cursor.getString(callLogIdColIdx)

            if (subscribedSimID == preferenceManager.getSIMSubscriptionId() ||
                subscribedSimID.dropLast(1) == preferenceManager.getSIMSubscriptionIccId()
                    ?.dropLast(1)
            ) {
                allCallLogsList!!.add(
                    SampleEntity(
                        name,
                        number,
                        date,
                        duration,
                        type,
                        subscribedSimID,
                        callLogId
                    )
                )
                Log.d("MY_APP_CALL_LOGS", "$number $duration $type $name")
                Log.d("subscription_Id", "$subscribedSimID and number = $number")

                when (type) {
                    "1" -> {
                        incomingCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId))
                    }
                    "2" -> {
                        outGoingCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId))
                    }
                    "3" -> {
                        missedCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId))
                    }
                    "5", "10" -> {
                        rejectedCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId))
                    }
                }
            }
        }

        Log.d("MY_APP_CALL_LIST", "${allCallLogsList!!.size}")
        cursor.close()
    }

    fun createDate(int: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, int)
        return "${calendar.timeInMillis}"
    }

    fun getAllCallLogs(forToday: Boolean = false, allCallData: (ArrayList<SampleEntity>) -> Unit) {
        if (forToday) {
            refreshCallLogs("All") { allCallsList ->
                val todaysCallList: ArrayList<SampleEntity> = ArrayList()
                for (callLogInfo in allCallsList!!) {
                    if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(
                            createDate(0))) {
                        todaysCallList.add(callLogInfo)
                    }
                }
                allCallData(todaysCallList)
            }
        } else {
            refreshCallLogs("All") { allCallsList ->
                allCallData(allCallsList!!)
            }
        }
    }

    fun getMissedCallLogs(): ArrayList<SampleEntity>? {
        if (missedCallList == null)
            loadCallLogs()

        return missedCallList
    }

    fun getOutGoingCallLogs(allCallData: (ArrayList<SampleEntity>) -> Unit) {
        refreshCallLogs("Outgoing") { allCallsList ->
            allCallData(allCallsList!!)
        }
    }

    fun getIncomingCallLogs(): ArrayList<SampleEntity>? {
        if (incomingCallList == null)
            loadCallLogs()

        return incomingCallList
    }

    fun getRejectedCallLogs(): ArrayList<SampleEntity>? {
        if (rejectedCallList == null)
            loadCallLogs()

        return rejectedCallList
    }

    fun getNeverAttended(
        useHandler: Boolean? = false,
        callDetailsWithCount: (MutableList<CallDetailsWithCount>) -> Unit) {
        if (useHandler == true) {
            loadCallLogsForNeverAttendedAndNeverPickedUp("NeverAttended") { missedCallList ->
                Log.d("missedCallList", missedCallList.toString())
                val sortedList = missedCallList!!.sortedWith(compareBy { it.userNumber })
                val groups = mutableListOf<CallDetailsWithCount>()
                sortedList.forEach {
                    val last = groups.lastOrNull()
                    if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                        last!!.count++
                    } else {
                        groups.add(CallDetailsWithCount(it, 1))
                    }
                }
                callDetailsWithCount(groups)
            }
        } else {
            if (missedCallList == null)
                loadCallLogs()
            val sortedList = missedCallList!!.sortedWith(compareBy { it.userNumber })
            val groups = mutableListOf<CallDetailsWithCount>()
            sortedList.forEach {
                val last = groups.lastOrNull()
                if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                    last!!.count++
                } else {
                    groups.add(CallDetailsWithCount(it, 1))
                }
            }
            callDetailsWithCount(groups)
        }
    }

    fun getNeverPickedUpByClient(
        useHandler: Boolean? = false,
        callDetailsWithCount: (MutableList<CallDetailsWithCount>) -> Unit) {
        if (useHandler == true) {
            loadCallLogsForNeverAttendedAndNeverPickedUp("NeverPickedUpByClient") { outGoingCallList ->
                Log.d("outGoingCallList", outGoingCallList.toString())
                val sortedList = outGoingCallList!!.sortedWith(compareBy { it.userNumber })
                val groups = mutableListOf<CallDetailsWithCount>()
                sortedList.forEach {
                    if (it.callDuration!!.toInt() == 0) {
                        val last = groups.lastOrNull()
                        if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                            last!!.count++
                        } else {
                            groups.add(CallDetailsWithCount(it, 1))
                        }
                    }
                }
                callDetailsWithCount(groups)
            }
        } else {
            if (outGoingCallList == null)
                loadCallLogs()

            val sortedList = outGoingCallList!!.sortedWith(compareBy { it.userNumber })
            val groups = mutableListOf<CallDetailsWithCount>()
            sortedList.forEach {
                if (it.callDuration!!.toInt() == 0) {
                    val last = groups.lastOrNull()
                    if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                        last!!.count++
                    } else {
                        groups.add(CallDetailsWithCount(it, 1))
                    }
                }
            }
            callDetailsWithCount(groups)
        }
    }

    fun getOutgoingCallState(number: String?): Int {
        var output = 0
        for (callLogInfo in outGoingCallList!!) {
            if (callLogInfo.userNumber.equals(number)) {
                output++
            }
        }
        return output
    }

    fun getOutgoingCallStateDuration(number: String?): String {
        var output = 0L
        for (callLogInfo in outGoingCallList!!) {
            if (callLogInfo.userNumber.equals(number)) {
                output += callLogInfo.callDuration?.toLong()!!
            }
        }
        return "$output"
    }

    fun getAllCallsDuration(forToday: Boolean = false): String {
        var output = 0L
        val allCallLogsReversed = allCallLogsList?.reversed()
        if (forToday) {

            var lastCallTime = 0L
            for (callLogInfo in allCallLogsReversed!!) {
                if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(createDate(0))) {
                    Log.d("Is call date same?", GlobalMethods.convertMillisToDate(callLogInfo.time!!) + "= ${GlobalMethods.convertMillisToDate(createDate(0))} ")
                    if (callLogInfo.callType!!.toInt() != CallLog.Calls.MISSED_TYPE) {
                        var updatedCallDuration = 0L
                        val time1 = callLogInfo.time!!.toLong()
                        Log.d("time1", "$time1")
                        val time2 = lastCallTime
                        Log.d("time2", "$time2")
                        val diff = GlobalMethods.calculateTimeDifference(time1, time2)
                        Log.d("diff", diff.toString())

                        val durationInMillis = callLogInfo.callDuration?.toLong() ?.times(1000L)
                        Log.d("durationInMillis", durationInMillis.toString())
                        //To do = difference would be the last call ended and the next call started.
                        lastCallTime = time1.plus(durationInMillis!!)
//                        lastCallTime = time1
                        Log.d("lastCallTime", lastCallTime.toString())
                        if (diff > 180 || diff < -180){
                            updatedCallDuration += callLogInfo.callDuration?.toLong()?.plus(60L)!!
                            Log.d("IdealTimeAdded", "$diff and $updatedCallDuration ${diff > 180} and ${diff < -180}")
                        }
                        else{
                            updatedCallDuration += callLogInfo.callDuration?.toLong()?.plus(180L)!!
                            Log.d("ProcessTimeAdded", "$diff and $updatedCallDuration")
                        }
                        output += updatedCallDuration
                        Log.d("output", output.toString())
                    }
                }
            }
        } else {
            var lastCallTime = 0L
            for (callLogInfo in allCallLogsReversed!!) {
                if (callLogInfo.callType!!.toInt() != CallLog.Calls.MISSED_TYPE) {
                    var updatedCallDuration = 0L
                    val time1 = callLogInfo.time!!.toLong()
                    Log.d("time1", "$time1")
                    val time2 = lastCallTime
                    Log.d("time2", "$time2")
                    val diff = GlobalMethods.calculateTimeDifference(time1, time2)
                    Log.d("diff", diff.toString())

                    val durationInMillis = callLogInfo.callDuration?.toLong() ?.times(1000L)
                    Log.d("durationInMillis", durationInMillis.toString())
//                    lastCallTime = time1
                    lastCallTime = time1.plus(durationInMillis!!)
                    Log.d("lastCallTime", lastCallTime.toString())

                    if (diff > 180 || diff < -180){
                        updatedCallDuration += callLogInfo.callDuration?.toLong()?.plus(60L)!!
                        Log.d("IdealTimeAdded", "$diff and $updatedCallDuration")
                    }
                    else{
                        updatedCallDuration += callLogInfo.callDuration?.toLong()?.plus(180L)!!
                        Log.d("ProcessTimeAdded", diff.toString()+" and ${callLogInfo.callDuration}")
                    }
                    output += updatedCallDuration
                    Log.d("output", output.toString())
                }
            }
        }
        return "$output"
    }

    fun highestCaller(
        forToday: Boolean = false,
        highestCallerName: (String) -> Unit) {
        if (allCallLogsList == null) {
            loadCallLogs()
        }

        if (forToday) {
            var todaysCallList: ArrayList<SampleEntity> = ArrayList()
            for (callLogInfo in allCallLogsList!!) {
                if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(
                        createDate(0)
                    )
                ) {
                    todaysCallList.add(callLogInfo)
                }
            }
            val item = todaysCallList.groupBy { it }.maxByOrNull { it.value.size }?.key
            highestCallerName(item?.userName.toString())
        } else {
            val item = allCallLogsList?.groupBy { it }
                ?.maxByOrNull { it.value.size }
                ?.key

            highestCallerName(item?.userName.toString())
        }
    }

    fun getMissedCallState(number: String?): Int {
        var output = 0
        for (callLogInfo in missedCallList!!) {
            if (callLogInfo.userNumber.equals(number)) {
                output++
            }
        }
        return output
    }

    fun getTotalNotPickedUpByClientCount(forToday: Boolean = false): Int {
        if (outGoingCallList == null)
            loadCallLogs()

        var output = 0
        if (forToday) {
            for (callLogInfo in outGoingCallList!!) {
                if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(
                        createDate(0)
                    )
                ) {
                    if (callLogInfo.callDuration!!.toInt() == 0) {
                        output++
                    }
                }
            }

        } else {
            for (callLogInfo in outGoingCallList!!) {
                if (callLogInfo.callDuration!!.toInt() == 0) {
                    output++
                }
            }
        }
        return output
    }
    /**
     * In order to get the count of the never attended calls.
     */
    fun getNeverAttendedCallsCount(forToday: Boolean = false): Int {
        if (missedCallList == null)
            loadCallLogs()

        var output = 0
        if (forToday) {
            for (callLogInfo in missedCallList!!) {
                if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(
                        createDate(0)
                    )
                ) {
                    output++
                }
            }
        } else {
            for (callLogInfo in missedCallList!!) {
                output++
            }
        }
        return output

    }

    /**
     * In order to get the Highest Call Logs Count
     */
    fun getHighestCallLogsCount(
        forToday: Boolean = false,
        highestCallLogsCount: (MutableList<CallDetailsWithCount>) -> Unit) {
        if (allCallLogsList == null) {
            loadCallLogs()
        }
        var todaysCallList: ArrayList<SampleEntity> = ArrayList()
        for (callLogInfo in allCallLogsList!!) {
            if (GlobalMethods.convertMillisToDate(callLogInfo.time!!) == GlobalMethods.convertMillisToDate(
                    createDate(0)
                )
            ) {
                todaysCallList.add(callLogInfo)
            }
        }
        if (forToday) {
            val sortedList = todaysCallList.sortedWith(compareBy { it.userNumber })
            val groups = mutableListOf<CallDetailsWithCount>()
            sortedList.forEach {
                val last = groups.lastOrNull()
                if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                    last!!.count++
                } else {
                    groups.add(CallDetailsWithCount(it, 1))
                }
            }
            highestCallLogsCount(groups)
        } else {
            val sortedList = allCallLogsList!!.sortedWith(compareBy { it.userNumber })
            val groups = mutableListOf<CallDetailsWithCount>()
            sortedList.forEach {
                val last = groups.lastOrNull()
                if ("${last?.callDetails?.userNumber}" == "${it.userNumber}") {
                    last!!.count++
                } else {
                    groups.add(CallDetailsWithCount(it, 1))
                }
            }
            highestCallLogsCount(groups)
        }
    }

    /**
     * This function is used to get the latest call logs, using the HighOrder Function.
     */

    fun getLatestCallLog(shareLatestCallLog: (SampleEntity) -> Unit) {

        loadAllLatestCallLog { shareLatestCallLog(it) }
    }

    /**
     * In order to load all the latest call logs.
     */
    private fun loadAllLatestCallLog(latestLog: (SampleEntity) -> Unit) {

        val numberCol = CallLog.Calls.NUMBER
        val durationCol = CallLog.Calls.DURATION
        val typeCol = CallLog.Calls.TYPE // 1 - Incoming, 2 - Outgoing, 3 - Missed , 5- Rejected
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val subscribedSimIDCol = CallLog.Calls.PHONE_ACCOUNT_ID
        val callLogIdCol = CallLog.Calls._ID
        val projection =
            arrayOf(
                numberCol,
                durationCol,
                typeCol,
                nameCol,
                dateCol,
                subscribedSimIDCol,
                callLogIdCol)
        val mSelectionClause = CallLog.Calls.DATE + " BETWEEN ? AND ?"

        /**
         * In order to check whether the firstTimeRegistration date has been saved in the preference or not
         */
        val preferenceFirstTimeRegisteredDate = preferenceManager.isSavedFirstRegisterTimeStamp()
        Log.d("FirstTimeDate", "$preferenceFirstTimeRegisteredDate")
        //When saved use the same date, else create the current date or call an api in this case.
        if (preferenceFirstTimeRegisteredDate) {
            fromDate = preferenceManager.getFirstTimeRegisterMillis()
        } else {
//            fromDate = "1636455990849"
            fromDate = createDate(0)
            preferenceManager.saveFirstTimeRegisterMillis(fromDate!!)
            Log.d("fromDate2", fromDate!!)
        }
        //from date will be the date of registration, the user has signed up.
        val mSelectionArgs = arrayOf(fromDate, createDate(1))
        GlobalMethods.convertMillisToDateAndTime(fromDate!!)
        mSelectionArgs.forEach {
            Log.d("SelectionDate:", it!!)
        }


            android.os.Handler(Looper.getMainLooper()).postDelayed({
                val cursor = context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    projection, mSelectionClause, mSelectionArgs, CallLog.Calls.DATE + " DESC"
                )

//            val cursor = context.contentResolver.query(
//                CallLog.Calls.CONTENT_URI,
//                projection, mSelectionClause, mSelectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER)

                val numberColIdx = cursor!!.getColumnIndex(numberCol)
                val durationColIdx = cursor.getColumnIndex(durationCol)
                val typeColIdx = cursor.getColumnIndex(typeCol)
                val nameColIdx = cursor.getColumnIndex(nameCol)
                val dateColIdx = cursor.getColumnIndex(dateCol)
                val subscribedSimIDColIdx = cursor.getColumnIndex(subscribedSimIDCol)
                val callLogIdColIdx = cursor.getColumnIndex(callLogIdCol)



                  while (cursor.moveToNext()) {
                      val number = cursor.getString(numberColIdx)
                      val duration = cursor.getString(durationColIdx)
                      val type = cursor.getString(typeColIdx)
                      val name = cursor.getString(nameColIdx)
                      val date = cursor.getString(dateColIdx)
                      val subscribedSimID = cursor.getString(subscribedSimIDColIdx)
                      val callLogId = cursor.getString(callLogIdColIdx)

                      if (!lastLogCount) {
                          if (subscribedSimID == preferenceManager.getSIMSubscriptionId() ||
                              subscribedSimID.dropLast(1) == preferenceManager.getSIMSubscriptionIccId()
                                  ?.dropLast(1)
                          ) {
                              latestLog(
                                  SampleEntity(
                                      name,
                                      number,
                                      date,
                                      duration,
                                      type,
                                      subscribedSimID,
                                      callLogId
                                  )
                              )
                              lastLogCount = true
                              Log.d("MY_APP_CALL_LOGS", "$number $duration $type $name")
                              Log.d("subscription_Id", "$subscribedSimID and number = $number")
                          }
                      }
                  }

                cursor.close()
                lastLogCount = false
            }, 2000)

    }

    private fun loadCallLogsForNeverAttendedAndNeverPickedUp(
        callType: String,
        callData: (ArrayList<SampleEntity>?) -> Unit) {
        neverAttendedList = ArrayList()
        neverPickedUpList = ArrayList()

        val numberCol = CallLog.Calls.NUMBER
        val durationCol = CallLog.Calls.DURATION
        val typeCol = CallLog.Calls.TYPE // 1 - Incoming, 2 - Outgoing, 3 - Missed , 5- Rejected
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val subscribedSimIDCol = CallLog.Calls.PHONE_ACCOUNT_ID
        val callLogIdCol = CallLog.Calls._ID
        val projection =
            arrayOf(
                numberCol,
                durationCol,
                typeCol,
                nameCol,
                dateCol,
                subscribedSimIDCol,
                callLogIdCol)
        val mSelectionClause = CallLog.Calls.DATE + " BETWEEN ? AND ?"

        /**
         * In order to check whether the firstTimeRegistration date has been saved in the preference or not
         */
        val preferenceFirstTimeRegisteredDate = preferenceManager.isSavedFirstRegisterTimeStamp()
        Log.d("FirstTimeDate", "$preferenceFirstTimeRegisteredDate")
        //When saved use the same date, else create the current date or call an api in this case.
        if (preferenceFirstTimeRegisteredDate) {
            fromDate = preferenceManager.getFirstTimeRegisterMillis()
        } else {
//            fromDate = "1636455990849"
            fromDate = createDate(0)
            preferenceManager.saveFirstTimeRegisterMillis(fromDate!!)
            Log.d("fromDate2", fromDate!!)
        }

        //from date will be the date of registration, the user has signed up.
        val mSelectionArgs = arrayOf(fromDate, createDate(1))
        GlobalMethods.convertMillisToDateAndTime(fromDate!!)
        mSelectionArgs.forEach {
            Log.d("SelectionDate:", it!!)
        }

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection, mSelectionClause, mSelectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER)

            val numberColIdx = cursor!!.getColumnIndex(numberCol)
            val durationColIdx = cursor.getColumnIndex(durationCol)
            val typeColIdx = cursor.getColumnIndex(typeCol)
            val nameColIdx = cursor.getColumnIndex(nameCol)
            val dateColIdx = cursor.getColumnIndex(dateCol)
            val subscribedSimIDColIdx = cursor.getColumnIndex(subscribedSimIDCol)
            val callLogIdColIdx = cursor.getColumnIndex(callLogIdCol)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberColIdx)
                val duration = cursor.getString(durationColIdx)
                val type = cursor.getString(typeColIdx)
                val name = cursor.getString(nameColIdx)
                val date = cursor.getString(dateColIdx)
                val subscribedSimID = cursor.getString(subscribedSimIDColIdx)
                val callLogId = cursor.getString(callLogIdColIdx)

                if (subscribedSimID == preferenceManager.getSIMSubscriptionId() ||
                    subscribedSimID.dropLast(1) == preferenceManager.getSIMSubscriptionIccId()
                        ?.dropLast(1)) {
                    when (type) {
                        "2" -> {
                            neverPickedUpList!!.add(
                                SampleEntity(
                                    name,
                                    number,
                                    date,
                                    duration,
                                    type,
                                    subscribedSimID,
                                    callLogId
                                )
                            )

                        }
                        "3" -> {
                            neverAttendedList!!.add(
                                SampleEntity(
                                    name,
                                    number,
                                    date,
                                    duration,
                                    type,
                                    subscribedSimID,
                                    callLogId
                                )
                            )

                        }
                    }
                }
            }
            when (callType) {
                "NeverAttended" -> callData(neverAttendedList)
                "NeverPickedUpByClient" -> callData(neverPickedUpList)
            }
            cursor.close()
        }, 100)
    }

    private fun refreshCallLogs(callType: String, callData: (ArrayList<SampleEntity>?) -> Unit) {
        allCallLogsList = ArrayList()
        missedCallList = ArrayList()
        outGoingCallList = ArrayList()
        incomingCallList = ArrayList()
        rejectedCallList = ArrayList()

        val numberCol = CallLog.Calls.NUMBER
        val durationCol = CallLog.Calls.DURATION
        val typeCol = CallLog.Calls.TYPE // 1 - Incoming, 2 - Outgoing, 3 - Missed , 5- Rejected
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val subscribedSimIDCol = CallLog.Calls.PHONE_ACCOUNT_ID
        val callLogIdCol = CallLog.Calls._ID
        val projection =
            arrayOf(
                numberCol,
                durationCol,
                typeCol,
                nameCol,
                dateCol,
                subscribedSimIDCol,
                callLogIdCol
            )
        val mSelectionClause = CallLog.Calls.DATE + " BETWEEN ? AND ?"

        /**
         * In order to check whether the firstTimeRegistration date has been saved in the preference or not
         */
        val preferenceFirstTimeRegisteredDate = preferenceManager.isSavedFirstRegisterTimeStamp()
        Log.d("FirstTimeDate", "$preferenceFirstTimeRegisteredDate")
        //When saved use the same date, else create the current date or call an api in this case.
        if (preferenceFirstTimeRegisteredDate) {
            fromDate = preferenceManager.getFirstTimeRegisterMillis()
        } else {
//            fromDate = "1636455990849"
            fromDate = createDate(0)
            preferenceManager.saveFirstTimeRegisterMillis(fromDate!!)
            Log.d("fromDate2", fromDate!!)
        }

        //from date will be the date of registration, the user has signed up.
        val mSelectionArgs = arrayOf(fromDate, createDate(1))
        GlobalMethods.convertMillisToDateAndTime(fromDate!!)
        mSelectionArgs.forEach {
            Log.d("SelectionDate:", it!!)
        }

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, mSelectionClause, mSelectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val durationColIdx = cursor.getColumnIndex(durationCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)
        val nameColIdx = cursor.getColumnIndex(nameCol)
        val dateColIdx = cursor.getColumnIndex(dateCol)
        val subscribedSimIDColIdx = cursor.getColumnIndex(subscribedSimIDCol)
        val callLogIdColIdx = cursor.getColumnIndex(callLogIdCol)

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val duration = cursor.getString(durationColIdx)
            val type = cursor.getString(typeColIdx)
            val name = cursor.getString(nameColIdx)
            val date = cursor.getString(dateColIdx)
            val subscribedSimID = cursor.getString(subscribedSimIDColIdx)
            val callLogId = cursor.getString(callLogIdColIdx)

            Log.d("subscribedSimID", subscribedSimID.dropLast(1))
            Log.d("calllogid", callLogId)
            Log.d(
                "subscribedSimICCID",
                "${preferenceManager.getSIMSubscriptionIccId()?.dropLast(1)}"
            )

            if (subscribedSimID == preferenceManager.getSIMSubscriptionId() ||
                subscribedSimID.dropLast(1) == preferenceManager.getSIMSubscriptionIccId()
                    ?.dropLast(1)
            ) {
                allCallLogsList!!.add(
                    SampleEntity(
                        name,
                        number,
                        date,
                        duration,
                        type,
                        subscribedSimID,
                        callLogId
                    )
                )
                Log.d("MY_APP_CALL_LOGS", "$number $duration $type $name")
                Log.d("subscription_Id", "$subscribedSimID and number = $number")

                when (type) {
                    "1" -> {
                        incomingCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId
                            )
                        )
                    }
                    "2" -> {
                        outGoingCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId
                            )
                        )
                    }
                    "3" -> {
                        missedCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId
                            )
                        )
                    }
                    "5", "10" -> {
                        rejectedCallList!!.add(
                            SampleEntity(
                                name,
                                number,
                                date,
                                duration,
                                type,
                                subscribedSimID,
                                callLogId
                            )
                        )
                    }
                }
            }
        }
        when (callType) {
            "All" -> callData(allCallLogsList)
            "Outgoing" -> callData(outGoingCallList)
            "Incoming" -> callData(incomingCallList)
        }
        Log.d("MY_APP_CALL_LIST", "${allCallLogsList!!.size}")
        cursor.close()
    }


    /**
     * This Function Will return list of SubscriptionInfo.
     * It is being used to fetch the names of network provider, such as Airtel, Vodafone, etc.
     */
    fun getSimCardInfoName(context: Context): List<SubscriptionInfo>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager: SubscriptionManager =
                context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw Exception("Permission Not Granted -> Manifest.permission.READ_PHONE_STATE")
            }
            subscriptionManager.activeSubscriptionInfoList
        } else {
            null
        }
    }

    /**
     * In order to get the sim card icc Id's, using the telecom Manager.
     */
    @SuppressLint("MissingPermission")
    fun getSimCardInfo(context: Context): List<String> {
        val simIds: ArrayList<String> = ArrayList()
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager?
        telecomManager!!.callCapablePhoneAccounts.forEach {
            simIds.add(it.id)
        }

        return simIds
    }

}