package com.eazybe.callLogger.ui.CallLogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.ItemAdapterLayoutOneBinding
import com.eazybe.callLogger.helper.GlobalMethods.convertMillisToTime
import com.eazybe.callLogger.helper.GlobalMethods.convertSeconds

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
                    "2" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_outgoing_icon
                            )
                        )
                    }
                    "1" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_received_icon
                            )
                        )
                    }
                    "3" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_missed_icon
                            )
                        )
                    }
                    "5","10" -> {
                        callTypeImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.call_disabled
                            )
                        )
                    }
                }
                personName.text = callData.userName ?: "Unknown"
                phoneNumber.text = callData.userNumber
                callDuration.text = convertSeconds(callData.callDuration!!.toInt())
                callTime.text = convertMillisToTime(callData.time!!)

                imgMessage.setOnClickListener {
                    sendMessageToUser(callData)
                }
                imgWhatsApp.setOnClickListener {
                    sendWhatsAppToUser(callData)
                }
                imgCall.setOnClickListener {
                    callUser(callData)
                }
                deleteIcon.setOnClickListener {

//                    //Report Button
//                    ivReportIcon.setOnClickListener {
//                        val popupMenu = android.widget.PopupMenu(context, it)
//                        popupMenu.menuInflater.inflate(R.menu.report_menu, popupMenu.menu)
//                        popupMenu.setOnMenuItemClickListener {
//                            showUserReportDialog("${profile.id}")
//                            popupMenu.dismiss()
//                            true
//                        }
//                        popupMenu.show()
//                    }
                    deleteUser(callData)
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
    abstract fun deleteUser(callData: SampleEntity)
}