package com.eazybe.callLogger.ui.SignUpAndLogin

import android.os.Looper
import android.telephony.SubscriptionInfo
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.requests.GoogleSignUpRequest
import com.eazybe.callLogger.api.models.requests.RegisterRequest
import com.eazybe.callLogger.api.models.requests.UpdateOrgRequest
import com.eazybe.callLogger.api.models.requests.VerifyEmailOtp
import com.eazybe.callLogger.api.models.responses.*
import com.eazybe.callLogger.helper.AmplifyHelper
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegistrationAndLoginViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val baseRepository: BaseRepository,
    private val callLogsHelper: CallLogsHelper,
    private val amplifyHelper: AmplifyHelper,
    private val clientDetailAdapter: JsonAdapter<UserData>,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>
) :
    ViewModel() {

    private val _addOrgCodeResponse = MutableLiveData<Boolean>()
    val orgCodeResponse: LiveData<Boolean> = _addOrgCodeResponse

    private val _userAlreadyHasOrgCode = MutableLiveData<Boolean>()
    val userAlreadyHasOrgCode: LiveData<Boolean> = _userAlreadyHasOrgCode

    private val _userRegisteredResponse = MutableLiveData<CreateOrUpdateUserResponse>()
    val userRegisteredResponse: LiveData<CreateOrUpdateUserResponse> = _userRegisteredResponse

    private val _userAlreadyExists = MutableLiveData<CreateOrUpdateUserResponse>()
    val userAlreadyExists: LiveData<CreateOrUpdateUserResponse> = _userAlreadyExists


    private val _sendToOtpScreen = MutableLiveData<String>()
    val sendToOtpScreen: LiveData<String> = _sendToOtpScreen

    private val _resendOTP = MutableLiveData<Boolean>()
    val resendOTP: LiveData<Boolean> = _resendOTP

    private val _redirectToHomeActivity = MutableLiveData<Boolean>()
    val redirectToHomeActivity: LiveData<Boolean> = _redirectToHomeActivity

    private val _callBaseActivity = MutableLiveData<Boolean>()
    val callBaseActivity: LiveData<Boolean> = _callBaseActivity

    private val _configureAmplify = MutableLiveData<CreateOrUpdateUserResponse>()
    val configureAmplify: LiveData<CreateOrUpdateUserResponse> = _configureAmplify

    private val _userRegistered = MutableLiveData<Boolean>()
    val userRegistered: LiveData<Boolean> = _userRegistered

    private val _emailVerified = MutableLiveData<VerifyOTPResponse>()
    val emailVerified: LiveData<VerifyOTPResponse> = _emailVerified

    private val _updateUserResponse = MutableLiveData<UpdateUserDetailsResponse>()
    val updateUserResponse: LiveData<UpdateUserDetailsResponse> = _updateUserResponse

    private val _gmailVerified = MutableLiveData<Boolean>()
    val gmailVerified: LiveData<Boolean> = _gmailVerified


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

    private val _responseFailedOTP = MutableLiveData<Boolean>()
    val responseFailedOTP: LiveData<Boolean> = _responseFailedOTP

    private var orgId: Int? = null

//    fun registerNewUser(
//        clientNumber: String,
//        clientName: String,
//        selectedSIM: Int?,
//        subscriptionInfo: ArrayList<String>,
//        subscriptionInfoSub: ArrayList<String>? = null,
//        organizationCode: Int? = null
//    ) =
//        viewModelScope.launch {
//            val currentTimeInMillis = callLogsHelper.createDate(0)
//            //convert the millis to TimeStamp
//            val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
//            Log.d(
//                "currentTimeConvertedDuringSignup",
//                "$currentTimeConverted = $currentTimeInMillis"
//            )
//            baseRepository.registerUser(
//                RegisterRequest(
//                    1,
//                    clientNumber,
//                    clientName,
//                    currentTimeConverted,
//                    organizationCode
//                )
//            ).let { response ->
//                when (selectedSIM) {
//                    1 -> {
//                        preferenceManager.saveSIMSubscriptionId(subscriptionInfo[0])
//                        if (subscriptionInfoSub != null) {
//                            preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[0])
//                        }
//                        Log.d("subIdAndIccId1", "${subscriptionInfo[0]} | " + subscriptionInfo[0])
//                    }
//                    2 -> {
//                        preferenceManager.saveSIMSubscriptionId(subscriptionInfo[1])
//                        if (subscriptionInfoSub != null) {
//                            preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[1])
//                        }
//                        Log.d("subIdAndIccId2", "${subscriptionInfo[1]} | " + subscriptionInfo[1])
//                    }
//                }
//                if (response?.type == true) {
//                    preferenceManager.saveLoginState(true)
//                    val dataInString =
//                        clientDetailAdapter.toJson(response.data)
//
//                    preferenceManager.saveClientRegistrationData(dataInString)
//
//                    _userRegisteredResponse.value = response!!
//                    Log.d("Response", "User Registered")
//                } else {
//                    if (response?.type == false &&
//                        response.message == "User Existed. You Can't Process"
//                    ) {
//
//                        preferenceManager.saveLoginState(true)
//                        val dataInString =
//                            clientDetailAdapter.toJson(response.data)
//                        preferenceManager.saveClientRegistrationData(dataInString)
//                        //save to shared preferences
//                        _userAlreadyExists.value = response!!
//                        Log.d("Response", "User Already Exists")
//                    }
//                }
//
//            }
//        }

    private fun callCreateCallyzerUserAgain() {
        createOrUpdateCallyzerUser()
    }

    fun createOrUpdateCallyzerUser() = viewModelScope.launch {
        val currentTimeInMillis = callLogsHelper.createDate(0)
        //convert the millis to TimeStamp
        val currentTimeConverted = GlobalMethods.convertMillisToDateAndTime(currentTimeInMillis)
        Log.d(
            "currentTimeConvertedDuringSignup",
            "$currentTimeConverted = $currentTimeInMillis"
        )

        val clientEmailData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)

        baseRepository.registerUser(
            RegisterRequest(
                1,
                "",
                "",
                currentTimeConverted,
                clientEmailData!!.id
            )
        )?.let { response ->
            if (response?.type == true) {
                if (response.data?.loggingStartsFrom.isNullOrEmpty()) {
                    callCreateCallyzerUserAgain()
                } else {

                    preferenceManager.saveLoginState(true)

                    val dataInString =
                        clientDetailAdapter.toJson(response.data)

                    preferenceManager.saveClientRegistrationData(dataInString)

                    _configureAmplify.value = response

                }

            }
//            else {
//                if (response?.type == false &&
//                    response.message == "User Existed. You Can't Process"
//                ) {
//                    if (response.data?.loggingStartsFrom.isNullOrEmpty()) {
//                        callCreateCallyzerUserAgain()
//                    } else {
//                        preferenceManager.saveLoginState(true)
//                        val dataInString =
//                            clientDetailAdapter.toJson(response.data)
//                        preferenceManager.saveClientRegistrationData(dataInString)
//                        //save to shared preferences
//                        _configureAmplify.value = response
//                        /**
//                         * Save the user with workspace id in AWS datastore after the registration.
//                         */
//
//                    }
//
//                }
//            }
        }
    }

    fun createAmplifyUser(response: CreateOrUpdateUserResponse) {
        /**
         * Save the user with workspace id in AWS datastore after the registration.
         */
        amplifyHelper.createUser(
            "${response.data?.id}",
            "${response.data?.email}",
            "${response.data?.loggingStartsFrom}",
            "${response.data?.name}"
        ) {
            Log.d("AmplifyResponse", "$it")
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                _userRegisteredResponse.value = response
                Log.d("Response", "User Registered")
            }, 300)
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
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        baseRepository.getOrganizationDetails(orgCode).let { response ->
            if (response?.type == true) {
                orgId = response.orgData!![0].id
                userData?.orgName = response.orgData!![0].orgName
                val dataInString = clientDetailEmailAdapter.toJson(userData)
                preferenceManager.saveClientRegistrationDataEmail(dataInString)

                _validateOrgCode.value = orgId!!
            } else {
                _validateOrgCodeFailure.value = true
            }
        }
    }

    fun verifyEmailAndSendOtp(email: String) = viewModelScope.launch {
        baseRepository.sendEmailOtp(email).let { response ->
            if (response?.status == true) {
                _sendToOtpScreen.value = email
            }
        }
    }

    fun resendOtp(userEmail: String) = viewModelScope.launch {
        baseRepository.sendEmailOtp(userEmail).let { response ->
            if (response?.status == true) {
                _resendOTP.value = true
            } else {
                _resendOTP.value = false
            }
        }
    }

    /**
     * To verify email and otp, returns workspace details when true.
     */

    fun verifyEmailOtp(email: String, otp: Int) = viewModelScope.launch {
        val date = GlobalMethods.createDateToday()
        baseRepository.verifyEmailOtp(VerifyEmailOtp(otp, email, date))?.let { response ->
            if (response.status == true) {
                val dataInString = clientDetailEmailAdapter.toJson(response.workspaceDetails)
                preferenceManager.saveClientRegistrationDataEmail(dataInString)
                preferenceManager.saveLoginState(true)

                //step 1
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    _emailVerified.value = response
                }, 300)

            }else if (response.otpVerification == false){
                _responseFailedOTP.value = true
            }

        }
    }

    fun verifyGmailSignUp(email: String, otp: String) = viewModelScope.launch {
        val date = GlobalMethods.createDateToday()
        baseRepository.verifyGmailSignUp(GoogleSignUpRequest(otp, email, date))?.let { response ->
            if (response.status == true) {
                val dataInString = clientDetailEmailAdapter.toJson(response.workspaceData)
                preferenceManager.saveClientRegistrationDataEmail(dataInString)
                preferenceManager.saveLoginState(true)
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    _gmailVerified.value = true
                }, 300)

            } else {
                if (response.workspaceData != null) {
                    val dataInString = clientDetailEmailAdapter.toJson(response.workspaceData)
                    preferenceManager.saveClientRegistrationDataEmail(dataInString)
                    preferenceManager.saveLoginState(true)
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        _gmailVerified.value = true
                    }, 300)
                } else {
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        _gmailVerified.value = false
                    }, 300)
                }

            }
        }
    }

    fun callBaseActivity() {
        _callBaseActivity.value = true
    }

    fun updateUserDetails(name: String, multipartRequest: MultipartBody.Part?) =
        viewModelScope.launch {
            val userData =
                clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        val userNameReqBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
            baseRepository.updateUserDetails(multipartRequest, userData?.id!!, userNameReqBody)?.let {
                if (it.type == true) {
                    userData?.name = it.data?.name
                    userData?.profilePic = it.profilePic
                    val dataInString = clientDetailEmailAdapter.toJson(userData)
                    preferenceManager.saveClientRegistrationDataEmail(dataInString)

                    _updateUserResponse.value = it
                }
            }

        }

    fun checkNameAndOrganization() {
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        if (userData?.name.isNullOrEmpty()) {
            _redirectToHomeActivity.value = false
        } else {
            _redirectToHomeActivity.value = true
        }
    }

    fun addOrganization(orgCode: Int) = viewModelScope.launch {
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)

        baseRepository.updateUserOrganization(
            UpdateOrgRequest(orgId = orgCode, userId = userData?.id)
        ).let {
            if (it?.type == false && it.message == "User Already With An Organization") {
                _userAlreadyHasOrgCode.value = true
            } else if (it?.type == true) {
                userData?.orgId = orgCode
                val dataInString = clientDetailEmailAdapter.toJson(userData)
                preferenceManager.saveClientRegistrationDataEmail(dataInString)
                preferenceManager.saveOrgState(true)
                _addOrgCodeResponse.value = true
            }
        }
    }

}