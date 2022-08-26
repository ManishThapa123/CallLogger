package com.eazybe.callLogger.dependency

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.eazybe.callLogger.api.CallLoggerAPIInterface
import com.eazybe.callLogger.api.CallLoggerClient
import com.eazybe.callLogger.api.models.entities.Data
import com.eazybe.callLogger.api.models.responses.CreateOrUpdateUserResponse
import com.eazybe.callLogger.api.models.responses.RegisterData
import com.eazybe.callLogger.api.models.responses.UserData
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.container.CallLoggerApplication
import com.eazybe.callLogger.helper.AmplifyHelper
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
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


    @Provides
    @Singleton
    fun provideAmplifyHelper(
        @NotNull preferenceManager: PreferenceManager,
        @ApplicationContext context: Context
    ): AmplifyHelper = AmplifyHelper(context, preferenceManager, provideClientRegistrationEmailDataAdapter())

    /**
     * provides instance of adapter which serializes and deserialized [RegisterData]
     */
    @Provides
    @Singleton
    fun provideClientRegistrationDataAdapter(): JsonAdapter<UserData> =
        Moshi.Builder().build().adapter(UserData::class.java)

    /**
     * provides instance of adapter which serializes and deserialized [WorkspaceDetails]
     */
    @Provides
    @Singleton
    fun provideClientRegistrationEmailDataAdapter(): JsonAdapter<WorkspaceDetails> =
        Moshi.Builder().build().adapter(WorkspaceDetails::class.java)
}