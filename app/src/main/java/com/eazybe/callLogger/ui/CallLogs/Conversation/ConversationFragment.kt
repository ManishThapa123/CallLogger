package com.eazybe.callLogger.ui.CallLogs.Conversation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eazybe.callLogger.R

class ConversationFragment : Fragment() {

    companion object {
        fun newInstance() = ConversationFragment()
    }

    private lateinit var viewModel: ConversationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conversation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConversationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}