package com.twango.calllogger.ui.CallLogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twango.calllogger.databinding.FragmentCallLogs1Binding


class CallLogsFragment1 : Fragment() {

    private lateinit var binding: FragmentCallLogs1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCallLogs1Binding.inflate(inflater, container, false)
        return binding.root
    }

}