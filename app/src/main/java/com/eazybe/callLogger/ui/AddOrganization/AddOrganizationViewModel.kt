package com.eazybe.callLogger.ui.AddOrganization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.requests.UpdateOrgRequest
import com.eazybe.callLogger.api.models.responses.RegisterData
import com.eazybe.callLogger.api.models.responses.UserData
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrganizationViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val baseRepository: BaseRepository,
    private val clientDetailAdapter: JsonAdapter<UserData>,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>
) :
    ViewModel() {

    private val _validateOrgCode = MutableLiveData<Int>()
    val validateOrgCode: LiveData<Int> = _validateOrgCode

    private val _validateOrgCodeFailure = MutableLiveData<Boolean>()
    val validateOrgCodeFailure: LiveData<Boolean> = _validateOrgCodeFailure

    private val _addOrgCodeResponse = MutableLiveData<Boolean>()
    val orgCodeResponse: LiveData<Boolean> = _addOrgCodeResponse

    private val _userAlreadyHasOrgCode = MutableLiveData<Boolean>()
    val userAlreadyHasOrgCode: LiveData<Boolean> = _userAlreadyHasOrgCode

    private val _responseFailed = MutableLiveData<Boolean>()
    val responseFailed: LiveData<Boolean> = _responseFailed

    private val _orgState = MutableLiveData<Boolean>()
    val orgState: LiveData<Boolean> = _orgState

    private var orgId: Int? = null
    private var orgName: String? = null


    fun getOrganizationState() = viewModelScope.launch {
        val orgState = preferenceManager.getOrgState()
        _orgState.value = orgState
    }

    fun getOrganizationDetails(orgCode: String) = viewModelScope.launch {
        baseRepository.getOrganizationDetails(orgCode).let { response ->
            if (response?.type == true) {
                orgId = response.orgData!![0].id
                orgName = response.orgData!![0].orgName
                _validateOrgCode.value = orgId!!
            } else {
                _validateOrgCodeFailure.value = true
            }
        }
    }

    fun addOrganization(orgCode: Int) = viewModelScope.launch {

        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        baseRepository.updateUserOrganization(
            UpdateOrgRequest(orgId = orgCode, userId = userData?.id)).let {
                if (it?.type == false && it.message == "User Already With An Organization") {
                    _userAlreadyHasOrgCode.value = true
                } else if (it?.type == true) {
                    _addOrgCodeResponse.value = true
                    userData?.orgName = orgName
                    val dataInString = clientDetailEmailAdapter.toJson(userData)
                    preferenceManager.saveClientRegistrationDataEmail(dataInString)
                    orgName = null
                    preferenceManager.saveOrgState(true)
                }
        }
    }
}