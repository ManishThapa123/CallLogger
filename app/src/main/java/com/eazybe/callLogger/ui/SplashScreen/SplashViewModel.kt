package com.eazybe.callLogger.ui.SplashScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _permissionState = MutableLiveData<Boolean>()
    val permissionState: LiveData<Boolean> = _permissionState

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    fun getPermissionSavedState() = viewModelScope.launch {
        val permissionState = preferenceManager.getPermissionState()
        _permissionState.value = permissionState
    }

    fun getLoginState() = viewModelScope.launch {
        val loginState = preferenceManager.getLoginState()
        _loginState.value = loginState
    }
}