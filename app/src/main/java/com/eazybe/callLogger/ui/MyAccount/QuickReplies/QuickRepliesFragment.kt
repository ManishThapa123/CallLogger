package com.eazybe.callLogger.ui.MyAccount.QuickReplies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eazybe.callLogger.api.models.responses.QuickReplies.UserQuickRepliesMessage
import com.eazybe.callLogger.databinding.QuickRepliesFragmentBinding
import com.eazybe.callLogger.helper.GlobalMethods
import com.eazybe.callLogger.helper.GlobalMethods.sendQuickReplyTextAndImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickRepliesFragment : Fragment() {

    private lateinit var binding: QuickRepliesFragmentBinding
    private val viewModel: QuickRepliesViewModel by viewModels()
    private lateinit var quickRepliesAdapter: QuickRepliesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        setQuickRepliesAdapter()
    }

    private fun observeViewModel() {
        viewModel.apply {
            userQuickReplies.observe({ lifecycle }) { quickReplies ->
                quickRepliesAdapter.submitList(quickReplies)
            }

            showProgress.observe({ lifecycle }) {
                if (it) {
                    binding.pbProgress.visibility = View.VISIBLE
                } else {
                    binding.pbProgress.visibility = View.GONE
                }
            }

            downloadPDFResponse.observe({ lifecycle }) { response ->
                if (response != null) {
                    val writeToDisk = GlobalMethods.writeResponseBodyToDisk(
                        response.response.body(),
                        "${response.fileName}",
                        "${response.fileURL}",
                        requireContext(), requireActivity()
                    )

                    if (writeToDisk) {
                        val existingFile: Boolean = GlobalMethods.checkIfFileExists(
                            "${response.fileName}",
                            requireContext())

                        if (existingFile) {
                            GlobalMethods.sendQuickReplyTextAndPDFToWhatsApp(
                                "${response.title}",
                                "${response.message}",
                                requireActivity(),
                                "${response.fileURL}",
                                requireContext(),
                                "${response.fileName}"
                            )
                        }
                    }
                }
            }

            downloadFileResponse.observe({ lifecycle }) { response ->
                if (response != null) {
                    val writeToDisk = GlobalMethods.writeResponseBodyToDisk(
                        response.response.body(),
                        "${response.fileName}",
                        "${response.fileURL}",
                        requireContext(), requireActivity())

                    if (writeToDisk) {
                        val existingFile: Boolean = GlobalMethods.checkIfFileExists(
                            "${response.fileName}",
                            requireContext())

                        if (existingFile) {
                            GlobalMethods.sendQuickReplyTextAndFileToWhatsApp(
                                "${response.title}",
                                "${response.message}",
                                requireActivity(),
                                "${response.fileURL}",
                                requireContext(),
                                "${response.fileName}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setQuickRepliesAdapter() {
        quickRepliesAdapter = object : QuickRepliesAdapter({ onItemClick(it) }) {
            override fun sendQuickReplyToWhatsApp(userQuickReply: UserQuickRepliesMessage) {
                sendQuickRepliesToWhatsApp(userQuickReply)
            }

            override fun getContext(): Context {
                return requireContext()
            }

        }
    }

    private fun onItemClick(quickRepliesMessage: UserQuickRepliesMessage) {

    }

    private fun sendQuickRepliesToWhatsApp(userQuickRepliesMessage: UserQuickRepliesMessage) {
        if (userQuickRepliesMessage.fileUrl.isNullOrEmpty()) {
            GlobalMethods.sendQuickReplyTextToWhatsApp(
                "${userQuickRepliesMessage.quickReplyTitle}",
                "${userQuickRepliesMessage.quickReplyMessage}",
                requireActivity())
        } else {
            when (userQuickRepliesMessage.fileUrl?.takeLast(4)?.lowercase()) {
                ".pdf" -> {
                    val existingFile: Boolean = GlobalMethods.checkIfFileExists(
                        "${userQuickRepliesMessage.fileName}",
                        requireContext())

                    if (existingFile) {
                        GlobalMethods.sendQuickReplyTextAndPDFToWhatsApp(
                            "${userQuickRepliesMessage.quickReplyTitle}",
                            "${userQuickRepliesMessage.quickReplyMessage}",
                            requireActivity(),
                            "${userQuickRepliesMessage.fileUrl}",
                            requireContext(),
                            "${userQuickRepliesMessage.fileName}")
                    } else {
                        viewModel.downloadPDF(
                            userQuickRepliesMessage.fileUrl!!,
                            userQuickRepliesMessage.fileName!!,
                            userQuickRepliesMessage.quickReplyTitle!!,
                            userQuickRepliesMessage.quickReplyMessage!!
                        )
                    }

                }
                ".png", ".jpg", "jpeg", "webp" -> {
                    sendQuickReplyTextAndImage(
                        "${userQuickRepliesMessage.quickReplyTitle}",
                        "${userQuickRepliesMessage.quickReplyMessage}",
                        requireActivity(),
                        requireContext(),
                        "${userQuickRepliesMessage.fileUrl}",
                        "${userQuickRepliesMessage.fileName}"
                    )
                }

                else -> {
                    val existingFile: Boolean = GlobalMethods.checkIfFileExists(
                        "${userQuickRepliesMessage.fileName}",
                        requireContext()
                    )
                    if (existingFile) {
                        GlobalMethods.sendQuickReplyTextAndFileToWhatsApp(
                            "${userQuickRepliesMessage.quickReplyTitle}",
                            "${userQuickRepliesMessage.quickReplyMessage}",
                            requireActivity(),
                            "${userQuickRepliesMessage.fileUrl}",
                            requireContext(),
                            "${userQuickRepliesMessage.fileName}"
                        )
                    } else {
                        viewModel.downloadFile(
                            userQuickRepliesMessage.fileUrl!!,
                            userQuickRepliesMessage.fileName!!,
                            userQuickRepliesMessage.quickReplyTitle!!,
                            userQuickRepliesMessage.quickReplyMessage!!
                        )
                    }

                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuickRepliesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapterView()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        getQuickReplies()
    }

    private fun setOnClickListener() {
        binding.apply {
            createNew.setOnClickListener {
                val action =
                    QuickRepliesFragmentDirections.actionQuickRepliesFragmentToCreateQuickReplyFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun getQuickReplies() {
        viewModel.getQuickReplies()
    }

    private fun setAdapterView() {
        binding.rvQuickReplies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quickRepliesAdapter
        }
    }

}