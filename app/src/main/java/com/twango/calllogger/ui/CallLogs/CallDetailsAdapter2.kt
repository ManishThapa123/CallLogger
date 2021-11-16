package com.twango.calllogger.ui.CallLogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twango.callLogger.api.models.entities.CallDetailsWithCount
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.databinding.ItemAdapterLayoutTwoBinding

abstract class CallDetailsAdapter2 :
    ListAdapter<CallDetailsWithCount, CallDetailsAdapter2.ViewHolder>(object :
        DiffUtil.ItemCallback<CallDetailsWithCount>() {
        override fun areItemsTheSame(oldItem: CallDetailsWithCount, newItem: CallDetailsWithCount): Boolean {
            return "${oldItem.callDetails?.userNumber}" == "${newItem.callDetails?.userNumber}"
        }

        override fun areContentsTheSame(oldItem: CallDetailsWithCount, newItem: CallDetailsWithCount): Boolean {
            return "${oldItem.callDetails?.userNumber}" == "${newItem.callDetails?.userNumber}"
        }

    }) {

    private var context: Context? = null

    inner class ViewHolder(val binding: ItemAdapterLayoutTwoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(callData: CallDetailsWithCount, position: Int) {
            context = getContext()
            binding.apply {
                personName.text = callData.callDetails!!.userName ?: "Unknown"
                phoneNumber.text = callData.callDetails!!.userNumber
                totalCalls.text = "Total Calls: ${callData.count}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAdapterLayoutTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val callDetails = getItem(position)
        holder.bind(callDetails, position)
    }

    abstract fun getContext(): Context
}