package com.twango.calllogger.ui.Onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twango.calllogger.helper.PreferenceManager
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

}