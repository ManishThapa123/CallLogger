package com.eazybe.callLogger.ui.AddOrganization

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.eazybe.callLogger.databinding.ActivityAddOrganizationBinding
import com.eazybe.callLogger.helper.GlobalMethods
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrganizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddOrganizationBinding
    private val addOrganizationViewModel: AddOrganizationViewModel by viewModels()
    private var orgCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrganizationBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        observeViewModel()
        setOnClickListeners()
        checkValidationForOrganizationCode()
    }

    private fun observeViewModel() {
        addOrganizationViewModel.validateOrgCode.observe({ lifecycle }) { organizationCode ->
            orgCode = organizationCode
            binding.imageValidationCheck.visibility = View.VISIBLE
            binding.organizationCode.isEnabled = false
            binding.organizationCode.isClickable = false
        }

        addOrganizationViewModel.validateOrgCodeFailure.observe({ lifecycle }) {
            orgCode = null
            binding.imageValidationCheck.visibility = View.GONE
            binding.imageValidationFailure.visibility = View.VISIBLE
        }

        addOrganizationViewModel.orgCodeResponse.observe({ lifecycle }) {
            GlobalMethods.showMotionToast(
                this@AddOrganizationActivity,
                "Organization Added!",
                "Your call logs will be synced to your registered organization.",
                "success",
                this@AddOrganizationActivity
            )
        }

        addOrganizationViewModel.userAlreadyHasOrgCode.observe({lifecycle}){
            GlobalMethods.showMotionToast(
                this@AddOrganizationActivity,
                "Whoops!",
                "Looks like your number is already synced to an Organization.",
                "info",
                this@AddOrganizationActivity)
        }
        addOrganizationViewModel.responseFailed.observe({lifecycle}){
            GlobalMethods.showMotionToast(
                this@AddOrganizationActivity,
                "Whoops!",
                "Something went wrong. Please try again Later",
                "failure",
                this@AddOrganizationActivity)
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            addOrganization.setOnClickListener {
                if (organizationCode.text.toString().trim().isEmpty() && orgCode == null) {
                    GlobalMethods.showMotionToast(
                        this@AddOrganizationActivity,
                        "Whoops!",
                        "Please enter your organization code to continue.",
                        "failure",
                        this@AddOrganizationActivity
                    )
                } else if (orgCode == null) {
                    GlobalMethods.showMotionToast(
                        this@AddOrganizationActivity,
                        "Validation Error!",
                        "Enter Valid organization code in order to continue.",
                        "warning",
                        this@AddOrganizationActivity
                    )
                } else {
                    addOrganizationViewModel.addOrganization(orgCode!!)
                }
            }

            ibBack.setOnClickListener {
                onBackPressed()
            }
            txtDashBoardLink.setOnClickListener {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://eazybe.com/workspace")
                startActivity(openURL)
            }
        }
    }

    private fun checkValidationForOrganizationCode() {
        binding.apply {
            organizationCode.doAfterTextChanged {
                if (it?.length == 17) {
                    addOrganizationViewModel.getOrganizationDetails("${organizationCode.text}")
                } else {
                    binding.imageValidationCheck.visibility = View.GONE
                    binding.imageValidationFailure.visibility = View.GONE
                }
            }
        }
    }
}