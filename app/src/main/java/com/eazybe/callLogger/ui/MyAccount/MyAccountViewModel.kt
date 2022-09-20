package com.eazybe.callLogger.ui.MyAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.responses.UpdateUserDetailsResponse
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>,
    private val baseRepository: BaseRepository
) : ViewModel() {

    private val _userData = MutableLiveData<WorkspaceDetails>()
    val userData: LiveData<WorkspaceDetails> = _userData

    private val _callExpiry = MutableLiveData<String?>()
    val callExpiry: LiveData<String?> = _callExpiry

    private val _callExpiryTime = MutableLiveData<String?>()
    val callExpiryTime : LiveData<String?> = _callExpiryTime

    private val _whatsAppExpiry = MutableLiveData<String?>()
    val whatsAppExpiry: LiveData<String?> = _whatsAppExpiry

    private val _updateUserResponse = MutableLiveData<UpdateUserDetailsResponse>()
    val updateUserResponse: LiveData<UpdateUserDetailsResponse> = _updateUserResponse

    fun clearDataFromPreference() = viewModelScope.launch {
        preferenceManager.clearAllData()
    }

    fun getPreferenceData() = viewModelScope.launch {
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        _userData.value = userData!!
        val callExpiry = preferenceManager.getExpiryState()
//        val callExpiryDate = preferenceManager.getExpiryValidity()
        val whatsAppExpiry = preferenceManager.getWhatsAppExpiry()

        if (!callExpiry.isNullOrEmpty()) {

            when (callExpiry) {
                "Trial_Expired" -> {
                    _callExpiry.value = "Trial_Expired"
                }
                "Paid_Expired" -> {
                    _callExpiry.value = "Paid_Expired"
                }
                "Syncing" -> {
                    _callExpiry.value = "Syncing"
                }
                "Trial_Active" -> {
                    _callExpiry.value = "Trial_Active"
                    val callExpiryTime = preferenceManager.getExpiryTime()
                    _callExpiryTime.value = callExpiryTime

                }
            }
        } else
            _callExpiry.value = "Not_Started"

        if (!whatsAppExpiry.isNullOrEmpty())
            if (whatsAppExpiry.toInt() >= 0)
                _whatsAppExpiry.value = whatsAppExpiry + " Days Left"
            else
                _whatsAppExpiry.value = "Expired"
        else
            _whatsAppExpiry.value = "Expired"
    }

    fun updateUserDetails(name: String, multipartRequest: MultipartBody.Part?) =
        viewModelScope.launch {
            val userData =
                clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
            val userNameReqBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            baseRepository.updateUserDetails(multipartRequest, userData?.id!!, userNameReqBody)
                ?.let {
                    if (it.type == true) {
                        userData?.name = it.data?.name

                        if (!it.profilePic.isNullOrEmpty())
                        userData?.profilePic = it.profilePic


                        val dataInString = clientDetailEmailAdapter.toJson(userData)
                        preferenceManager.saveClientRegistrationDataEmail(dataInString)

                        _updateUserResponse.value = it
                    }
                }
        }
}