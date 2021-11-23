package com.twango.calllogger.container

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
@Suppress("DEPRECATION")
class CallLoggerApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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