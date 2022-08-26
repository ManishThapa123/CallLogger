package com.eazybe.callLogger.container

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.datastore.DataStoreConfiguration
import com.amplifyframework.datastore.generated.model.CallLogs
import com.amplifyframework.datastore.generated.model.Chatter
import com.amplifyframework.datastore.generated.model.User
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.PreferenceManager
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.smartlook.sdk.smartlook.Smartlook
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.HiltAndroidApp
import glimpse.core.Glimpse
import javax.inject.Inject

@HiltAndroidApp
@Suppress("DEPRECATION")
class CallLoggerApplication : Application(), LifecycleObserver {
    @Inject
    lateinit var clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails?>

    override fun onCreate() {
        super.onCreate()
        Glimpse.init(this)
        Stetho.initializeWithDefaults(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        FirebaseApp.initializeApp(this)
        generateTokenFirebase()
        /**
         *  Initialization of Smartlook inside the Application object
         *  Uses the SDK key provided in the smart look dashboard.
         */
        Smartlook.setupAndStartRecording("4456f686f5ae65eddf67078efe44635fc1f29d6f")

        /**
         * To initialize Amplify when your application is launched
         */

        val preferenceManager = PreferenceManager(this)
        val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()

        var convertedUserData: WorkspaceDetails? = if (!prefSavedUserData.isNullOrEmpty()) {
            clientDetailEmailAdapter.fromJson(prefSavedUserData)
        } else null

        try {
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSDataStorePlugin())

            Log.i("MyAmplifyApp", "Initialized Amplify")

            if (convertedUserData != null){
                Amplify.addPlugin(AWSDataStorePlugin.builder().dataStoreConfiguration(
                    DataStoreConfiguration.builder()
                        .syncExpression(User::class.java) { User.USERID.eq("${convertedUserData.id}") }
                        .syncExpression(Chatter::class.java) { Chatter.CREATED_BY_USER.eq("${convertedUserData.id}") }
                        .syncExpression(CallLogs::class.java) { CallLogs.CREATED_BY_USER.eq("${convertedUserData.id}") }
                        .build())
                    .build())
            }
            Amplify.configure(applicationContext)

        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    private fun generateTokenFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result.toString()

                // Log and toast
                Log.d("TokenFCM", token)
                println("device id :: $token")
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startSomething() {
        Log.d("ProcessLog", "APP IS ON FOREGROUND")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopSomething() {
        Log.d("ProcessLog", "APP IS IN BACKGROUND")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseSomething() {
        Log.d("ProcessLog", "APP IS IN Paused State")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroySomething() {
        Log.d("ProcessLog", "APP IS IN destroyed State")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumeSomething() {
        Log.d("ProcessLog", "APP IS IN resumed State")
    }

    fun isAppInitialized(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.INITIALIZED
    }

    fun isAppCreated(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.CREATED
    }

    fun isAppResumed(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.RESUMED
    }

    fun isAppStarted(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }

    fun isAppDestroyed(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.DESTROYED
    }

    fun getCurrentState(): String {
        return "${ProcessLifecycleOwner.get().lifecycle.currentState}"
    }
// save when destroyed through preference
    // check on pause , onstop state of the application/ activity

}