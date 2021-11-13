package com.twango.calllogger.helper

import android.content.Context
import android.provider.CallLog
import android.util.Log
import com.twango.callLogger.api.models.entities.SampleEntity
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
        val projection = arrayOf(numberCol, durationCol, typeCol, nameCol, dateCol)
        val mSelectionClause = CallLog.Calls.DATE + " BETWEEN ? AND ?"

        /**
         * In order to check whether the firstTimeRegistration date has been saved in the preference or not
         */
        val preferenceFirstTimeRegisteredDate = preferenceManager.isSavedFirstRegisterTimeStamp()
        Log.d("FirstTimeDate", "$preferenceFirstTimeRegisteredDate")
        //When saved use the same date, else create the current date or call an api in this case.
        if (preferenceFirstTimeRegisteredDate) {
            fromDate = preferenceManager.getFirstTimeRegisterMillis()
        }else{
            fromDate = "1636455990849"
//            fromDate = createDate(0)
//            preferenceManager.saveFirstTimeRegisterMillis(fromDate)
//            Log.d("fromDate2", fromDate)
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

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val duration = cursor.getString(durationColIdx)
            val type = cursor.getString(typeColIdx)
            val name = cursor.getString(nameColIdx)
            val date = cursor.getString(dateColIdx)
            allCallLogsList!!.add(SampleEntity(name, number, date, duration, type))
            Log.d("MY_APP_CALL_LOGS", "$number $duration $type $name")

            when (type) {
                "1" -> {
                    incomingCallList!!.add(SampleEntity(name, number, date, duration, type))
                }
                "2" -> {
                    outGoingCallList!!.add(SampleEntity(name, number, date, duration, type))
                }
                "3" -> {
                    missedCallList!!.add(SampleEntity(name, number, date, duration, type))
                }
                "5" -> {
                    rejectedCallList!!.add(SampleEntity(name, number, date, duration, type))
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

    fun getAllCallLogs(): ArrayList<SampleEntity>? {
        if (allCallLogsList == null)
            loadCallLogs()

        return allCallLogsList
    }

    fun getMissedCallLogs(): ArrayList<SampleEntity>? {
        if (missedCallList == null)
            loadCallLogs()

        return missedCallList
    }

    fun getOutGoingCallLogs(): ArrayList<SampleEntity>? {
        if (outGoingCallList == null)
            loadCallLogs()

        return outGoingCallList
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


//    fun getAllCallState(number: String?): LongArray? {
//        val output = LongArray(2)
//        for (callLogInfo in mainList) {
//            if (callLogInfo.getNumber().equals(number)) {
//                output[0]++
//                if (callLogInfo.getCallType()
//                        .toInt() != CallLog.Calls.MISSED_TYPE
//                ) output[1] += callLogInfo.getDuration()
//            }
//        }
//        return output
//    }

//    fun getIncomingCallState(number: String?): LongArray? {
//        val output = LongArray(2)
//        for (callLogInfo in incomingCallList!!) {
//            if (callLogInfo.getNumber().equals(number)) {
//                output[0]++
//                output[1] += callLogInfo.getDuration()
//            }
//        }
//        return output
//    }

//    fun getOutgoingCallState(number: String?): LongArray? {
//        val output = LongArray(2)
//        for (callLogInfo in outGoingCallList!!) {
//            if (callLogInfo.userNumber.equals(number)) {
//                output[0]++
//                output[1] += callLogInfo.callDuration
//            }
//        }
//        return output
//    }

    fun getMissedCallState(number: String?): Int {
        var output = 0
        for (callLogInfo in missedCallList!!) {
            if (callLogInfo.userNumber.equals(number)) {
                output++
            }
        }
        return output
    }

}