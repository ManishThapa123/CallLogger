package com.twango.calllogger.ui.CallLogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twango.callLogger.api.models.entities.SampleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(): ViewModel() {

    private val _sampleData = MutableLiveData<List<SampleEntity>>()
    val sampleData: LiveData<List<SampleEntity>> = _sampleData

    fun getCallLogs() = viewModelScope.launch {
    val callDetails = listOf(SampleEntity(
        "Manish Thapa",
        "+9779803847565",
        "12:05 PM",
        "05 sec",
        "OUTGOING"
    ),
        SampleEntity(
            "Imran Pandey",
            "+919676695371",
            "11:55 PM",
            "55 sec",
            "INCOMING"
        ),
        SampleEntity(
            "Mohammed Siddharth",
            "+919910375709",
            "10:00 AM",
            "24 sec",
            "MISSED"
        ),
    )
        _sampleData.value = callDetails
    }
}