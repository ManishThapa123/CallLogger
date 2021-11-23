package com.twango.calllogger.dependency

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.twango.callLogger.api.CallLoggerAPIInterface
import com.twango.callLogger.api.CallLoggerClient
import com.twango.callLogger.api.models.entities.Data
import com.twango.calllogger.container.CallLoggerApplication
import com.twango.calllogger.helper.CallLogsHelper
import com.twango.calllogger.helper.PreferenceManager
import com.twango.calllogger.repository.BaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjection {

    /**
     * provides instance of api client interface [APIClient.service]
     */
    @Provides
    @Singleton
    fun provideApiClient(): CallLoggerAPIInterface = CallLoggerClient.service

    /**
     * provides instance of base repository [BaseRepository]
     */
    @Provides
    @Singleton
    fun providesBaseRepository(
        apiInterface: CallLoggerAPIInterface,
        preferenceManager: PreferenceManager
    ): BaseRepository =
        BaseRepository(apiInterface, preferenceManager)

    /**
     * provides instance of preference manager [PreferenceManager]
     */
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager =
        PreferenceManager(context)

    /**
     * provides instance of preference manager [PreferenceManager]
     */
    @Provides
    @Singleton
    fun provideApplication(): CallLoggerApplication =
       CallLoggerApplication()

    /**
     * provides instance of CallLogsHelper
     */
    @Provides
    @Singleton
    fun provideCallLogs(
        @NotNull preferenceManager: PreferenceManager,
        @ApplicationContext context: Context
    ): CallLogsHelper = CallLogsHelper(context, preferenceManager)

    /**
     * provides instance of adapter which serializes and deserialized [Data]
     */
    @Provides
    @Singleton
    fun provideClientRegistrationDataAdapter(): JsonAdapter<Data> =
        Moshi.Builder().build().adapter(Data::class.java)
}