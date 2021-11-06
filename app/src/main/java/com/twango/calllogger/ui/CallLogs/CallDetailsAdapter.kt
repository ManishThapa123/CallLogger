package com.twango.calllogger.ui.CallLogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.R
import com.twango.calllogger.databinding.ItemAdapterLayoutOneBinding

abstract class CallDetailsAdapter : ListAdapter<SampleEntity,
        CallDetailsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<SampleEntity>() {
    override fun areItemsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
        return "${oldItem.userNumber}" == "${newItem.userNumber}"
    }

    override fun areContentsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
        return "${oldItem.userNumber}" == "${newItem.userNumber}"
    }

}) {
    private var context: Context? = null

    inner class ViewHolder(val binding: ItemAdapterLayoutOneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(callData: SampleEntity, position: Int) {
            context = getContext()
            binding.apply {
                when (callData.callType) {
                    "OUTGOING" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_outgoing_icon
                            )
                        )
                    }
                    "INCOMING" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_received_icon
                            )
                        )
                    }
                    "MISSED" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_missed_icon
                            )
                        )
                    }
                    "REJECTED" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_not_interested
                            )
                        )
                    }
                }
                personName.text = callData.userName
                phoneNumber.text = callData.userNumber
                callDuration.text = callData.callDuration
                callTime.text = callData.time

                imgMessage.setOnClickListener {
                    sendMessageToUser(callData)
                }
                imgWhatsApp.setOnClickListener {
                    sendWhatsAppToUser(callData)
                }
                imgCall.setOnClickListener {
                    callUser(callData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAdapterLayoutOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val callData = getItem(position)
        holder.bind(callData, position)
    }

    abstract fun getContext(): Context
    abstract fun sendMessageToUser(callData: SampleEntity)
    abstract fun sendWhatsAppToUser(callData: SampleEntity)
    abstract fun callUser(callData: SampleEntity)
}