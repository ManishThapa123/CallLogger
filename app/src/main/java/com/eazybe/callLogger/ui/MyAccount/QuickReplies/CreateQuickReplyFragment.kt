package com.eazybe.callLogger.ui.MyAccount.QuickReplies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazybe.callLogger.databinding.FragmentCreateQuickReplyBinding
import com.eazybe.callLogger.helper.FileUtil
import com.eazybe.callLogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class CreateQuickReplyFragment : Fragment() {
    private lateinit var binding: FragmentCreateQuickReplyBinding
    private val viewModel: QuickRepliesViewModel by viewModels()

    private lateinit var docPicker: ActivityResultLauncher<Intent>

    private var dpFile: File? = null
    private var multipartRequest: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.apply {
            quickReplyCreated.observe({lifecycle}){
                if (it){
                    GlobalMethods.showMotionToast(requireActivity(),
                    "Success", "Quick Reply has been created", "success", requireContext())
                    activity?.onBackPressed()
                }
            }

            showProgress2.observe({lifecycle}){
                if (it){
                    binding.pbProgress.visibility = View.VISIBLE
                }else
                    binding.pbProgress.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuickReplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        docPicker = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val uri = data?.data
                dpFile = FileUtil.from(requireContext(), uri)
                binding.apply {
                    txtAttachmentName.text = dpFile!!.name
                    txtAttachmentName.maxLines = 1
                }
                multipartRequest =  uploadFile(dpFile!!)

            }
        }

    }

    private fun setOnClickListener() {
        binding.apply {
            btnAttachmentBrowse.setOnClickListener {
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                intent = Intent.createChooser(intent, "Choose a file")
                docPicker.launch(intent)
            }

            btnCreate.setOnClickListener {
                binding.apply {
                    when {
                        txtQuickReply.text.isNullOrEmpty() -> {
                            GlobalMethods.showToast(requireContext(), "Title Required")
                        }
                        txtMessage.text.isNullOrEmpty() -> {
                            GlobalMethods.showToast(requireContext(), "Message Required")
                        }
                        else -> {
                        viewModel.createQuickReply("${txtQuickReply.text}","${txtMessage.text}",multipartRequest)
                        }
                    }
                }
            }
            ibBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    /**
     * upload the image to the backend
     * @param file -> file to be uploaded to the backend
     */
    private fun uploadFile(file: File) : MultipartBody.Part {
        /**
         * firstly convert the file to request body
         * then create a multi part body part form data
         */

        val requestFile: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartReq: MultipartBody.Part =
            MultipartBody.Part.createFormData("quickReplyFile", file.name, requestFile)
//        val userIdReqBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        return multipartReq

    }


}