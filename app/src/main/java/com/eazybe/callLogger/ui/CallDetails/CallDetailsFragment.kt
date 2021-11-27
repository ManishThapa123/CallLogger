package com.eazybe.callLogger.ui.CallDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eazybe.callLogger.R

class CallDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CallDetailsFragment()
    }

    private lateinit var viewModel: CallDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.call_details_fragment, container, false)
    }


}