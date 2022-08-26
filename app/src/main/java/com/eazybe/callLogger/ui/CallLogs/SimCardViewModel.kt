package com.eazybe.callLogger.ui.CallLogs

import android.telephony.SubscriptionInfo
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.requests.SaveSyncCallsRequestItem
import com.eazybe.callLogger.api.models.responses.UserData
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.AmplifyHelper
import com.eazybe.callLogger.helper.CallLogsHelper
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimCardViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val callLogsHelper: CallLogsHelper,
    private val baseRepository: BaseRepository,
    private val clientDetailAdapter: JsonAdapter<UserData>,
    private val amplifyHelper: AmplifyHelper,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>
) : ViewModel() {

    private val _simCardInfo = MutableLiveData<ArrayList<SubscriptionInfo>>()
    val simCardInfo: LiveData<ArrayList<SubscriptionInfo>> = _simCardInfo

    private val _simCardInfoTelecom = MutableLiveData<ArrayList<String>>()
    val simCardInfoTelecom: LiveData<ArrayList<String>> = _simCardInfoTelecom

    private val _simCardInfoSub = MutableLiveData<ArrayList<String>>()
    val simCardInfoSub: LiveData<ArrayList<String>> = _simCardInfoSub

    private val _goToCallLogsFragment = MutableLiveData<Boolean>()
    val goToCallLogsFragment: LiveData<Boolean> = _goToCallLogsFragment

    private val _goToHome = MutableLiveData<Boolean>()
    val goToHome: LiveData<Boolean> = _goToHome

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


    fun saveSimDataInPref(
        selectedSIM: Int,
        subscriptionInfo: ArrayList<String>,
        subscriptionInfoSub: ArrayList<String>
    ) {
        when (selectedSIM) {
            1 -> {
                preferenceManager.saveSIMSubscriptionId(subscriptionInfo[0])
                if (subscriptionInfoSub != null) {
                    preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[0])
                }
                Log.d("subIdAndIccId1", "${subscriptionInfo[0]} | " + subscriptionInfo[0])
                _goToCallLogsFragment.value = true
            }
            2 -> {
                preferenceManager.saveSIMSubscriptionId(subscriptionInfo[1])
                if (subscriptionInfoSub != null) {
                    preferenceManager.saveSIMSubscriptionIdSub(subscriptionInfoSub[1])
                }
                Log.d("subIdAndIccId2", "${subscriptionInfo[1]} | " + subscriptionInfo[1])
                _goToCallLogsFragment.value = true
            }
        }
    }

    fun saveSyncItems()  = viewModelScope.launch{
        //To start syncing the call logs.
        val prefSavedUserData = preferenceManager.getClientRegistrationDataEmail()
        val convertedUserData = clientDetailEmailAdapter.fromJson(prefSavedUserData!!)

        baseRepository.saveSyncItems(SaveSyncCallsRequestItem("call", convertedUserData!!.id, ""))?.let {
            Log.d("response", "${it.data}")

          val time =  GlobalMethods.convertSyncedDateToMillis(it.data?.get(0)?.syncStartedAt!!)

            Log.d("time", "$time")
//            preferenceManager.saveLastSyncedTime(it.data?.get(0)?.syncStartedAt!!)

            _goToHome.value = true
        }
    }
}