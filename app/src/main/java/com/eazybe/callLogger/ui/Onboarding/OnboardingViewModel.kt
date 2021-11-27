package com.eazybe.callLogger.ui.Onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.helper.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    fun savePermissionAccessStateToPreference() = viewModelScope.launch {
        preferenceManager.savePermissionState(true)
    }

    fun clearDataFromPreference() = viewModelScope.launch {
        preferenceManager.clearAllData()
    }

}