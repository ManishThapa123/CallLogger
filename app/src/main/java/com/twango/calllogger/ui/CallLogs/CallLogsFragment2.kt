package com.twango.calllogger.ui.CallLogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twango.calllogger.databinding.FragmentCallLogs2Binding

/**
 * A simple [Fragment] subclass.
 * Use the [CallLogsFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallLogsFragment2 : Fragment() {
    private lateinit var binding: FragmentCallLogs2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCallLogs2Binding.inflate(inflater, container, false)
        return binding.root
    }

}