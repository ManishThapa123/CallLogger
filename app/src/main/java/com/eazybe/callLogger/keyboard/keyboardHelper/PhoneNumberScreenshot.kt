package com.eazybe.callLogger.keyboard.keyboardHelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.eazybe.callLogger.keyboard.view.MyKeyboardView

object PhoneNumberScreenshot {

    private val _phoneNumber = MutableLiveData<String?>()
    val phoneNumber: LiveData<String?> = _phoneNumber


    fun passPhoneNumberToKeyboard(phoneNumber: String){
        _phoneNumber.value = phoneNumber
    }

    fun removeObserver() {
        _phoneNumber.value = null
    }
}