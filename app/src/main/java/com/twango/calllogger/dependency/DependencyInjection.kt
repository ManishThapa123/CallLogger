package com.twango.calllogger.dependency

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.twango.calllogger.helper.CallLogsHelper
import com.twango.calllogger.helper.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjection {

    /**
     * provides instance of preference manager [PreferenceManager]
     */
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager =
        PreferenceManager(context)


    /**
     * provides instance of CallLogsHelper
     */
    @Provides
    @Singleton
    fun provideCallLogs(
        @NotNull preferenceManager: PreferenceManager,
        @ApplicationContext context: Context): CallLogsHelper = CallLogsHelper(context, preferenceManager)

}