package com.eazybe.callLogger.ui.MyAccount.QuickReplies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazybe.callLogger.api.models.responses.QuickReplies.FileDownloadedResponse
import com.eazybe.callLogger.api.models.responses.QuickReplies.UserQuickRepliesMessage
import com.eazybe.callLogger.api.models.responses.WorkspaceDetails
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.PreferenceManager
import com.eazybe.callLogger.repository.BaseRepository
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuickRepliesViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val clientDetailEmailAdapter: JsonAdapter<WorkspaceDetails>,
    private val baseRepository: BaseRepository
) : ViewModel() {

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress : LiveData<Boolean> = _showProgress

    private val _showProgress2 = MutableLiveData<Boolean>()
    val showProgress2 : LiveData<Boolean> = _showProgress2

    private val _userQuickReplies = MutableLiveData<List<UserQuickRepliesMessage>?>()
    val userQuickReplies: LiveData<List<UserQuickRepliesMessage>?> = _userQuickReplies

    private val _downloadPDFResponse = MutableLiveData<FileDownloadedResponse>()
    val downloadPDFResponse: LiveData<FileDownloadedResponse> = _downloadPDFResponse

    private val _downloadFileResponse = MutableLiveData<FileDownloadedResponse>()
    val downloadFileResponse: LiveData<FileDownloadedResponse> = _downloadFileResponse

    private val _quickReplyCreated = MutableLiveData<Boolean>()
    val quickReplyCreated: LiveData<Boolean> = _quickReplyCreated

    fun getQuickReplies() = viewModelScope.launch {
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)

        baseRepository.fetchUserQuickReply("${userData?.id}")?.let { response ->
        if (response.status == true){
            _userQuickReplies.value = response.userQuickRepliesMessages
        }
        }
    }

    fun downloadPDF(fileUrl: String, fileName: String, title: String, message: String) = viewModelScope.launch {
        _showProgress.value = true
        baseRepository.downloadFileWithDynamicUrl(fileUrl).let { response ->
            if (response.isSuccessful){
                val fileDownloadedResponse = FileDownloadedResponse(response = response,fileName = fileName, fileURL = fileUrl, title, message)
                _showProgress.value = false
                _downloadPDFResponse.value = fileDownloadedResponse
//                GlobalMethods.writeResponseBodyToDisk(response.body(),fileName,fileUrl)
            }
        }
    }

    fun downloadFile(fileUrl: String, fileName: String, title: String, message: String) = viewModelScope.launch {
        _showProgress.value = true
        baseRepository.downloadFileWithDynamicUrl(fileUrl).let { response ->
            if (response.isSuccessful){
                val fileDownloadedResponse = FileDownloadedResponse(response = response,fileName = fileName, fileURL = fileUrl, title, message)
                _showProgress.value = false
                _downloadFileResponse.value = fileDownloadedResponse
            }
        }
    }

    fun createQuickReply(title: String, message: String, photo: MultipartBody.Part?) = viewModelScope.launch {
        _showProgress2.value = true
        val userData =
            clientDetailEmailAdapter.fromJson(preferenceManager.getClientRegistrationDataEmail()!!)
        if (photo != null){
            baseRepository.createNewQuickReplyMessage(photo,title,message,"${userData?.id}")?.let {
                if (it.status == true){
                    _showProgress2.value = false
                    _quickReplyCreated.value = true
                }
            }
        }else{
            baseRepository.createNewQuickReplyMessageWithoutPhoto(title,message,"${userData?.id}")?.let {
                if (it.status == true){
                    _showProgress2.value = false
                    _quickReplyCreated.value = true
                }
            }
        }
    }

}