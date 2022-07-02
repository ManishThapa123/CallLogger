package com.eazybe.callLogger.ui.FollowUps

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eazybe.callLogger.R

class FollowUpFragment : Fragment() {

    companion object {
        fun newInstance() = FollowUpFragment()
    }

    private lateinit var viewModel: FollowUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follow_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}