package com.twango.calllogger.ui.SignUpAndLogin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.twango.callLogger.api.models.entities.Data
import com.twango.callLogger.api.models.responses.LastSyncResponse
import com.twango.callLogger.api.models.responses.RegistrationResponse
import com.twango.calllogger.helper.CallLogsHelper
import com.twango.calllogger.helper.GlobalMethods
import com.twango.calllogger.helper.PreferenceManager
import com.twango.calllogger.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationAndLoginViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val baseRepository: BaseRepository,
    private val callLogsHelper: CallLogsHelper,
    private val clientDetailAdapter: JsonAdapter<Data>
) :
    ViewModel() {

    private val _userRegisteredResponse = MutableLiveData<RegistrationResponse>()
    val userRegisteredResponse: LiveData<RegistrationResponse> = _userRegisteredResponse

    private val _userAlreadyExists = MutableLiveData<RegistrationResponse>()
    val userAlreadyExists: LiveData<RegistrationResponse> = _userAlreadyExists

    private val _userLastSync = MutableLiveData<LastSyncResponse>()
    val userLastSync: LiveData<LastSyncResponse> = _userLastSync

    fun registerNewUser(clientNumber: String, clientName: String) =
        viewModelScope.launch {
            val currentTimeInMillis = callLogsHelper.createDate(0)
            //convert the millis to TimeStamp
            val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
            Log.d("currentTimeConvertedDuringSignup",
                "$currentTimeConverted = $currentTimeInMillis")
            baseRepository.registerUser(clientNumber, clientName, currentTimeConverted)
                .let { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.type == true) {
                            _userRegisteredResponse.value = responseBody!!
                            preferenceManager.saveLoginState(true)
                            val dataInString = clientDetailAdapter.toJson(responseBody.data)
                            preferenceManager.saveClientRegistrationData(dataInString)

                            Log.d("Response", "User Registered")
                        } else {
                            if (responseBody?.message == "user existed")
                                preferenceManager.saveLoginState(true)

                            val dataInString = clientDetailAdapter.toJson(responseBody?.data)
                            preferenceManager.saveClientRegistrationData(dataInString)
                            //save to shared preferences
                            _userAlreadyExists.value = responseBody!!
                            Log.d("Response", "User Already Exists")
                        }
                    } else {
                        Log.d("Response", "Failed to fetch result")
                    }
                }
        }

    fun checkLastSynced(clientNumber: String) = viewModelScope.launch {
        baseRepository.checkLastSync(clientNumber).let { response ->
            if (response.isSuccessful) {
                if (response.body()?.type == true) {
                    if (!response.body()?.data?.clientMobile.isNullOrEmpty()) {
                        _userLastSync.value = response.body()
                        preferenceManager.saveLoginState(true)
                        //save to shared preferences
                        val dataInString = clientDetailAdapter.toJson(response.body()?.data)
                        preferenceManager.saveClientRegistrationData(dataInString)
                    }
                }
            }
        }
    }
}