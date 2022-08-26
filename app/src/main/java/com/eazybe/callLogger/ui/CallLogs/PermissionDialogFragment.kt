package com.eazybe.callLogger.ui.CallLogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.PermissionDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PermissionDialogFragment : DialogFragment() {

    private lateinit var binding: PermissionDialogFragmentBinding
    private val simCardViewModel: SimCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PermissionDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialog?.setCancelable(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //when a user decides to sync their call logs.
        binding.btnOk.setOnClickListener {
            dismiss()
//            simCardViewModel.saveSyncItems()
        }
        simCardViewModel.goToHome.observe({ lifecycle }) {
            findNavController().navigate(R.id.gotoCallLogFragment)
            dismiss()
        }
    }
}
