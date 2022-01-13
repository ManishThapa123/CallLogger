package com.eazybe.callLogger.ui.SignUpAndLogin

import android.telephony.SubscriptionInfo
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.requests.RegisterRequest
import com.eazybe.callLogger.api.models.responses.LastSyncResponse
import com.eazybe.callLogger.api.models.responses.RegisterData
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
    private val clientDetailAdapter: JsonAdapter<RegisterData>
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

    private val _simCardInfoSub = MutableLiveData<ArrayList<String>>()
    val simCardInfoSub: LiveData<ArrayList<String>> = _simCardInfoSub

    private val _validateOrgCode = MutableLiveData<Int>()
    val validateOrgCode: LiveData<Int> = _validateOrgCode

    private val _validateOrgCodeFailure = MutableLiveData<Boolean>()
    val validateOrgCodeFailure: LiveData<Boolean> = _validateOrgCodeFailure

    private val _responseFailed = MutableLiveData<Boolean>()
    val responseFailed: LiveData<Boolean> = _responseFailed

    private var orgId: Int? = null

    fun registerNewUser(
        clientNumber: String,
        clientName: String,
        selectedSIM: Int?,
        subscriptionInfo: ArrayList<String>,
        subscriptionInfoSub: ArrayList<String>? = null,
        organizationCode: Int? = null
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
                RegisterRequest(
                    1,
                    clientNumber,
                    clientName,
                    currentTimeConverted,
                    organizationCode
                )
            ).let { response ->
                when (selectedSIM) {
                    1 -> {
                        preferenceManager.saveSIMSubscriptionId(subscriptionInfo[0])
                        if (subscriptionInfoSub != null) {
                            preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[0])
                        }
                        Log.d("subIdAndIccId1", "${subscriptionInfo[0]} | " + subscriptionInfo[0])
                    }
                    2 -> {
                        preferenceManager.saveSIMSubscriptionId(subscriptionInfo[1])
                        if (subscriptionInfoSub != null) {
                            preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[1])
                        }
                        Log.d("subIdAndIccId2", "${subscriptionInfo[1]} | " + subscriptionInfo[1])
                    }
                }
                if (response?.type == true) {
                    preferenceManager.saveLoginState(true)
                    val dataInString =
                        clientDetailAdapter.toJson(response.registerData!![0])
                    preferenceManager.saveClientRegistrationData(dataInString)
                    _userRegisteredResponse.value = response!!
                    Log.d("Response", "User Registered")
                } else {
                    if (response?.type == false &&
                        response.message == "User Existed. You Can't Process"
                    ) {
                        preferenceManager.saveLoginState(true)
                        val dataInString =
                            clientDetailAdapter.toJson(response.registerData!![0])
                        preferenceManager.saveClientRegistrationData(dataInString)
                        //save to shared preferences
                        _userAlreadyExists.value = response!!
                        Log.d("Response", "User Already Exists")
                    }
                }

            }
        }


    fun getSimCardInfos(context: FragmentActivity) = viewModelScope.launch {

        callLogsHelper.getSimCardInfo(context) { simInfoNew ->
//            GlobalMethods.showToast(context,"$simInfoNew")
            Log.d("simInfoNew", "${simInfoNew.size} and $simInfoNew")
            _simCardInfoTelecom.value = simInfoNew
        }

        callLogsHelper.getSimCardSubIdOnly(context) { simSubInfoNew ->
//            GlobalMethods.showToast(context,"$simSubInfoNew")
            _simCardInfoSub.value = simSubInfoNew
        }

        //For Sim Card Name
        callLogsHelper.getSimCardInfoName(context).let {
            val simCardInfos = it as ArrayList<SubscriptionInfo>
            _simCardInfo.value = simCardInfos
        }
    }


    fun getOrganizationDetails(orgCode: String) = viewModelScope.launch {
        baseRepository.getOrganizationDetails(orgCode).let { response ->
            if (response?.type == true) {
                orgId = response.orgData!![0].id
                _validateOrgCode.value = orgId!!
            } else {
                _validateOrgCodeFailure.value = true
            }
        }
    }
}