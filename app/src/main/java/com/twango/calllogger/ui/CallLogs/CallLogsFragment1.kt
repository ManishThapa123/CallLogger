package com.twango.calllogger.ui.CallLogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.twango.calllogger.databinding.FragmentCallLogs1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogsFragment1 : Fragment() {

    private lateinit var binding: FragmentCallLogs1Binding
    private lateinit var callDetailsAdapter: CallDetailsAdapter
    private val callDetailsViewModel: CallLogsViewModel by viewModels()

    companion object {
        fun newInstance(type: String): Fragment {
            val bundle = bundleOf("type" to type)
            val fragment = CallLogsFragment1()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callDetailsViewModel.getCallLogs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCallLogs1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCallLogsAdapter()
        observeViewModel()
    }

    //To attach the adapter
    private fun setCallLogsAdapter() {
        callDetailsAdapter = object : CallDetailsAdapter() {
            override fun getContext(): Context {
                return requireContext()
            }
        }
        binding.rvCallDetailsFragmentOne.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = callDetailsAdapter
        }
    }

    private fun observeViewModel(){
        callDetailsViewModel.sampleData.observe({lifecycle}){sampleData ->
            callDetailsAdapter.submitList(sampleData)
        }
    }
}