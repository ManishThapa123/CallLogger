package com.twango.calllogger.ui.CallLogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twango.callLogger.api.models.entities.SampleEntity
import com.twango.calllogger.databinding.ItemAdapterLayoutTwoBinding

abstract class CallDetailsAdapter2 :
    ListAdapter<SampleEntity, CallDetailsAdapter2.ViewHolder>(object :
        DiffUtil.ItemCallback<SampleEntity>() {
        override fun areItemsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
            return "${oldItem.userNumber}" == "${newItem.userNumber}"
        }

        override fun areContentsTheSame(oldItem: SampleEntity, newItem: SampleEntity): Boolean {
            return "${oldItem.userNumber}" == "${newItem.userNumber}"
        }

    }) {

    private var context: Context? = null

    inner class ViewHolder(val binding: ItemAdapterLayoutTwoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(callData: SampleEntity, position: Int) {
            context = getContext()
            binding.apply {
                personName.text = callData.userName
                phoneNumber.text = callData.userNumber
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