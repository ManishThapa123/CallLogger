package com.eazybe.callLogger.ui.SignUpAndLogin

import android.telephony.SubscriptionInfo
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.entities.Data
import com.eazybe.callLogger.api.models.responses.LastSyncResponse
import com.eazybe.callLogger.api.models.responses.RegistrationResponse
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
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

    private val _simCardInfo = MutableLiveData<ArrayList<SubscriptionInfo>>()
    val simCardInfo: LiveData<ArrayList<SubscriptionInfo>> = _simCardInfo

    private val _simCardInfoTelecom = MutableLiveData<ArrayList<String>>()
    val simCardInfoTelecom: LiveData<ArrayList<String>> = _simCardInfoTelecom

    fun registerNewUser(
        clientNumber: String,
        clientName: String,
        selectedSIM: Int?,
        subscriptionInfo: ArrayList<String>,
        organizationCode: String
    ) =
        viewModelScope.launch {
            val currentTimeInMillis = callLogsHelper.createDate(0)
            //convert the millis to TimeStamp
            val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
            Log.d(
                "currentTimeConvertedDuringSignup",
                "$currentTimeConverted = $currentTimeInMillis"
            )
            baseRepository.registerUser(
                clientNumber,
                clientName,
                currentTimeConverted,
                organizationCode
            )
                .let { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        when (selectedSIM) {
                            1 -> {
                                preferenceManager.saveSIMSubscriptionId(subscriptionInfo[0])
                                preferenceManager.saveSIMSubscriptionIccId(subscriptionInfo[0])
                                Log.d(
                                    "subIdAndIccId1",
                                    "${subscriptionInfo[0]} | " + subscriptionInfo[0]
                                )
                            }
                            2 -> {
                                preferenceManager.saveSIMSubscriptionId(subscriptionInfo[1])
                                preferenceManager.saveSIMSubscriptionIccId(subscriptionInfo[1])
                                Log.d(
                                    "subIdAndIccId2",
                                    "${subscriptionInfo[1]} | " + subscriptionInfo[1]
                                )
                            }
                        }
                        if (responseBody?.type == true) {
                            preferenceManager.saveLoginState(true)
                            val dataInString = clientDetailAdapter.toJson(responseBody.data)
                            preferenceManager.saveClientRegistrationData(dataInString)
                            _userRegisteredResponse.value = responseBody!!
                            Log.d("Response", "User Registered")
                        } else {
                            if (responseBody?.message == "user existed") {
                                preferenceManager.saveLoginState(true)
                                val dataInString = clientDetailAdapter.toJson(responseBody.data)
                                preferenceManager.saveClientRegistrationData(dataInString)
                                //save to shared preferences
                                _userAlreadyExists.value = responseBody!!
                                Log.d("Response", "User Already Exists")
                            }
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

    fun getSimCardInfos(context: FragmentActivity) = viewModelScope.launch {
        val simInfoNew = callLogsHelper.getSimCardInfo(context)
        Log.d("simInfoNew", "${simInfoNew.size}, and $simInfoNew")
        _simCardInfoTelecom.value = simInfoNew as ArrayList<String>
        callLogsHelper.getSimCardInfoName(context).let {
            val simCardInfos = it as ArrayList<SubscriptionInfo>
            _simCardInfo.value = simCardInfos

        }
    }
}