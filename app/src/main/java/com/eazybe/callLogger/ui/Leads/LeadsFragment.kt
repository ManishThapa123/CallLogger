package com.eazybe.callLogger.ui.Leads

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eazybe.callLogger.R

class LeadsFragment : Fragment() {

    companion object {
        fun newInstance() = LeadsFragment()
    }

    private lateinit var viewModel: LeadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.leads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LeadsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}