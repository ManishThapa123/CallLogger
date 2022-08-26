package com.eazybe.callLogger.ui.SignUpAndLogin.Onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.eazybe.callLogger.databinding.FragmentOnboardingNameBinding
import com.eazybe.callLogger.extensions.getProgressDrawable
import com.eazybe.callLogger.extensions.loadImage
import com.eazybe.callLogger.helper.FileUtil
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.ui.SignUpAndLogin.OtpScreenFragmentDirections
import com.eazybe.callLogger.ui.SignUpAndLogin.RegistrationAndLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class OnboardingFragmentName : Fragment() {
    private lateinit var binding: FragmentOnboardingNameBinding
    private val registrationAndLoginViewModel: RegistrationAndLoginViewModel by viewModels()

    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>
    private var dpFile: File? = null
    private var multipartRequest: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
                val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
                binding.imgPerson.loadImage(uriFilePath, getProgressDrawable(requireContext()))
                dpFile = FileUtil.from(requireContext(), uriContent)
               multipartRequest =  uploadImage(dpFile!!)
            } else {
                // an error occurred
                val exception = result.error
                Log.e("Error In Photo Upload", "$exception")
                GlobalMethods.showToast(requireContext(), "Something went wrong with the upload.")
            }
        }

        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.txtChangePicture.setOnClickListener {
            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setAspectRatio(3, 4)
                    setFixAspectRatio(true)
                }
            )
        }

        binding.continueButton.setOnClickListener {
            when {
                binding.txtName.text.toString().isEmpty() -> {
                    binding.txtName.error = "Field required"
                }
                else -> {

                    registrationAndLoginViewModel.updateUserDetails(binding.txtName.text.toString(), multipartRequest)
                }
                }
        }
    }

    /**
     * upload the image to the backend
     * @param file -> file to be uploaded to the backend
     */
    private fun uploadImage(file: File) : MultipartBody.Part {
        /**
         * firstly convert the file to request body
         * then create a multi part body part form data
         */

        val requestFile: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartReq: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)
//        val userIdReqBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        return multipartReq

//        pageThreeViewModel.uploadPicture(token, multipartReq)
    }

    private fun observeViewModel(){
        registrationAndLoginViewModel.updateUserResponse.observe({lifecycle}){

            //move to organization fragment
            val action =
                OnboardingFragmentNameDirections.actionOnboardingFragmentNameToOnboardingFragmentOrganization()
            findNavController().navigate(action)
        }
    }

}