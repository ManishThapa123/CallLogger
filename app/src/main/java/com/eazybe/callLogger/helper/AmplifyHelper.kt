package com.eazybe.callLogger.helper

import android.content.Context
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelPagination
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Page
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.CallLogs
import com.amplifyframework.datastore.generated.model.Chatter
import com.amplifyframework.datastore.generated.model.User
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.extensions.toast
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AmplifyHelper @Inject constructor(
    private val context: Context,
    private val preferenceManager: PreferenceManager,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var pbActive = false

    fun createUser(
        userId: String,
        email: String,
        lastSync: String,
        name: String,
        userData: (DataStoreItemChange<User>) -> Unit
    ) {
        val localDate =
            LocalDateTime.parse(lastSync, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val milliSec = localDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        Log.d("DateTimeinMillis", "$milliSec")

        val date = Date(milliSec)
        val offsetMillis = TimeZone.getDefault().getOffset(date.time)
        val offsetSeconds = TimeUnit.MILLISECONDS.toSeconds(offsetMillis.toLong()).toInt()
        val temporalDateTime: Temporal.DateTime = Temporal.DateTime(date, offsetSeconds)

        val post = User.builder()
            .userid(userId)
            .email(email)
            .lastSync(temporalDateTime)
            .name(name)
            .build()

        Amplify.DataStore.save(post,
            {
                Log.i("MyAmplifyApp", "Created a new post successfully $it")
                userData(it)
            },
            {
                Log.e("MyAmplifyApp", "Error creating post", it)
                createUser(userId, email, lastSync, name){item->
                    userData(item)
                }
            }
        )
    }

    private fun createChatter(
        name: String, createdByUser: String, phoneNumber: String, direction: String,
        chatterData: (DataStoreItemChange<Chatter>) -> Unit
    ) {

        val chatter = Chatter.builder()
            .chatterId("$createdByUser$phoneNumber")
            .createdByUser(createdByUser)
            .name(name)
            .number(phoneNumber)
            .direction(direction)
            .build()

        Amplify.DataStore.save(chatter,
            {
                Log.i("MyAmplifyAppChatter", "Created a new Chatter successfully $it")
                chatterData(it)
            },
            { failure ->
                Log.e("MyAmplifyAppChatter", "Error creating Chatter", failure)
            })
    }

    fun saveCallLog(
        duration: Int,
        userId: String,
        chatId: String,
        direction: String,
        callTime: Double,
        createdByUser: String,
        callLogData: (DataStoreItemChange<CallLogs>) -> Unit
    ) {

        val date = Date()
        val offsetMillis = TimeZone.getDefault().getOffset(date.time)
        val offsetSeconds = TimeUnit.MILLISECONDS.toSeconds(offsetMillis.toLong()).toInt()
        val temporalDateTime: Temporal.DateTime = Temporal.DateTime(date, offsetSeconds)

        val callLog = CallLogs.builder()
            .duration(duration)
            .userid(userId)
            .chatid(chatId)
            .datetime(temporalDateTime)
            .direction(direction)
            .calltime(callTime)
            .createdByUser(createdByUser)
            .build()

        Amplify.DataStore.save(callLog,
            {
                Log.i("MyAmplifyAppCallLog", "Created a new callLog successfully $it")
                callLogData(it)
            },
            { failure ->
                Log.e("MyAmplifyAppCallLog", "Error creating a new callLog", failure)
            })
    }


    private fun checkOrCreateChatter(
        createdByUser: String, name: String, phoneNumber: String, direction: String,
        chatterData: (Chatter) -> Unit
    ) {
        Amplify.API.query(
            ModelQuery.list(
                Chatter::class.java,
                Chatter.CREATED_BY_USER.eq(createdByUser).and(Chatter.NUMBER.eq(phoneNumber))
            ),
            { response ->
                val listOfData = response.data.toList()

                listOfData.forEach { data ->
                    Log.d("listOfData", "${data.name} and ${data.createdByUser}")
                }
                //problem is here.

                if (listOfData.isNullOrEmpty()) {
                    Log.i("MyAmplifyApi", "UnSuccessful query, Empty.")
                    createChatter(name, createdByUser, phoneNumber,direction) { chatterData ->
                        chatterData(chatterData.item())
                    }
                } else {
                    Log.i("MyAmplifyApi", "Data is there.")
                    Log.i("MyAmplifyApi", "Successful query, found posts.")
                    listOfData.forEach { chatter ->
                        chatterData(chatter)
                    }
                }
            },

            {
                Log.e("MyAmplifyApi", "Error retrieving posts", it)

            }
        )
    }

    fun checkUserLastSynced(
        callDetails: ArrayList<SampleEntity>
    ) {
        //To check whether the chatter has been created or not
        val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()
        val convertedUserData =
            clientDetailEmailAdapter.fromJson(prefSavedUserData!!)

        Log.d("convertedID", "${convertedUserData?.id}")
        var user: User? = null

        Amplify.DataStore.query(CallLogs::class.java,

            Where.matches(CallLogs.CREATED_BY_USER.eq("${convertedUserData?.id}")).sorted(CallLogs.CALLTIME.descending())
                .paginated(Page.startingAt(0).withLimit(10)),
            { result ->
                Log.i("MyAmplifyAppRead", "CallLogs Retrieve Success")
                val callLogslist = result.asSequence().toList()
                Log.i("MyAmplifyAppReadList", "CallLogs Retrieve Success $callLogslist")
                val callLogsWithTime = ArrayList<CallLogs>()

                if (!callLogslist.isNullOrEmpty()) {
                    callLogslist.forEach { callLog ->
                        if (callLog.calltime == null || callLog.calltime == 0.toDouble()) {
                            Log.i("MyAmplifyAppRead", "CallLogs is without time.")
                        } else {
                            callLogsWithTime.add(callLog)
                        }
                    }
                    val lastCallSyncTime = callLogsWithTime.first().calltime.toLong()
                    Log.i("MyAmplifyAppReadLastSyncTime", "CallLogs $lastCallSyncTime")

                    //Also check it it is greater than the syncStartedAtTime
                    val filteredList = callDetails.filter { it.time!!.toLong() > lastCallSyncTime }

                    val updatedFilteredList = filteredList.filter { it.time!!.toLong() > preferenceManager.getLastSyncedTimeAmplify()!!.toLong() }

                    updatedFilteredList.forEach { callLog ->
                        if (!pbActive) {
//                            context.toast("Syncing")
                            pbActive = true
                        }

                        Log.d("filteredList", "${callLog.userName} and ${callLog.time}")
                        //To check whether the Chatter already exists and if not then create a new chatter and save the call logs.
                        checkOrCreateChatter(
                            "${convertedUserData?.id}",
                            "${callLog.userName}",
                            "${callLog.userNumber}",
                            direction = callLog.callType ?: "0"
                        ) { chatter ->
                            saveCallLog(
                                callLog.callDuration?.toInt() ?: 0,
                                userId = "${convertedUserData?.id}",
                                chatId = chatter.id ?: "null",
                                direction = callLog.callType ?: "0",
                                callTime = callLog.time?.toDouble() ?: 0.toDouble(),
                                createdByUser = chatter.createdByUser
                            ) { dataStoreItem ->
                                Log.d(
                                    "Call Synced with", "Synced"
                                )
                            }
                        }
                    }

                    pbActive = false
                } else {
                    Amplify.DataStore.query(User::class.java,
                        Where.matches(User.USERID.eq("${convertedUserData?.id}")),
                        { users ->
                            if (users.hasNext()) {
                                //convert lastSync to millis
                                user = users.next()
                            }
                            val cal: Calendar = Calendar.getInstance()
                            cal.time = user?.lastSync!!.toDate()
//                            val millis: Long = cal.timeInMillis

                            val millis: Long = preferenceManager.getLastSyncedTimeAmplify()?.toLong()?: cal.timeInMillis
                            //check in preference

                            Log.i("Millis", "$millis")
                            val filteredList = callDetails.filter { it.time!!.toLong() > millis }
                            //To check whether the chatter has been created or not
                            filteredList.forEach { callLog ->
                                Log.d("filteredList", "${callLog.userName} and ${callLog.time}")
                                //To check whether the Chatter already exists and if not then create a new chatter and save the call logs.
                                checkOrCreateChatter(
                                    "${convertedUserData?.id}",
                                    "${callLog.userName}",
                                    "${callLog.userNumber}",
                                    direction = callLog.callType ?: "0"
                                ) { chatter ->

                                    saveCallLog(
                                        callLog.callDuration?.toInt() ?: 0,
                                        userId = "${convertedUserData?.id}",
                                        chatId = chatter.id ?: "null",
                                        direction = callLog.callType ?: "0",
                                        callTime = callLog.time?.toDouble() ?: 0.toDouble(),
                                        createdByUser = chatter.createdByUser
                                    ) { dataStoreItem ->
                                        Log.d("Call Synced with", "Synced")
                                    }
                                }
                            }
                        },
                        { Log.e("MyAmplifyApp", "Error retrieving posts", it) }
                    )
                }
            },
            {
                Log.e("MyAmplifyAppRead", "CallLogs Retrieve Failure")
            })
    }

    fun updateUsersLastSyncTime(lastSync: String) {

        val temporalDateTime = GlobalMethods.convertMillisToTempDate(lastSync)

        Amplify.DataStore.query(User::class.java,
            { matches ->
                if (matches.hasNext()) {
                    val original = matches.next()
                    val edited = original.copyOfBuilder()
                        .lastSync(temporalDateTime)
                        .build()

                    Amplify.DataStore.save(edited,
                        { Log.i("MyAmplifyApp", "MyAmplifyApp a post") },
                        { Log.e("MyAmplifyApp", "Update failed", it) }
                    )
                }
            },
            { Log.e("MyAmplifyApp", "Query failed", it) }
        )
    }
}