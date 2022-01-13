package com.eazybe.callLogger.ui.CallLogs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.api.models.entities.SampleEntity
import com.eazybe.callLogger.R
import com.eazybe.callLogger.databinding.ItemAdapterLayoutOneBinding
import com.eazybe.callLogger.helper.GlobalMethods.convertMillisToTime
import com.eazybe.callLogger.helper.GlobalMethods.convertSeconds
import com.judemanutd.autostarter.AutoStartPermissionHelper

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
                        callTypeImg.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.ic_outgoing
                            )
                        )
                        callType.text = "Outgoing Call"
                    }
                    "1" -> {
                        callTypeImg.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.ic_incoming
                            )
                        )
                        callType.text = "Incoming Call"
                    }
                    "3" -> {
                        callTypeImg.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.ic_missed
                            )
                        )
                        callType.text = "Missed Call"
                    }
                    "5","10" -> {
                        callTypeImg.setImageDrawable(
                            ContextCompat.getDrawable(
                                context!!,
                                R.drawable.ic_rejected))
                        callType.text = "Rejected Call"
                    }
                }
                callTypeImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.person_image
                    )
                )
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
                imgCopyContact.setOnClickListener {
                    copyContact(callData)
                }
//                deleteIcon.setOnClickListener {
//
////                    //Report Button
////                    ivReportIcon.setOnClickListener {
////                        val popupMenu = android.widget.PopupMenu(context, it)
////                        popupMenu.menuInflater.inflate(R.menu.report_menu, popupMenu.menu)
////                        popupMenu.setOnMenuItemClickListener {
////                            showUserReportDialog("${profile.id}")
////                            popupMenu.dismiss()
////                            true
////                        }
////                        popupMenu.show()
////                    }
//
//                    val builder = AlertDialog.Builder(context!!)
//                    builder.setTitle("Delete Call Log")
//                    builder.setMessage("Deleting would permanently remove the call log from the phone book. Continue?")
//                    builder.setPositiveButton("Delete") { _, _ ->
//                        deleteUser(callData)
//                        builder.create().dismiss()
//                    }
//                    builder.setNegativeButton("Cancel"){_,_ ->
//                        builder.create().dismiss()
//                    }
//                    builder.setCancelable(true)
//                    builder.create()
//                    builder.show()
//
//                }
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
    abstract fun copyContact(callData: SampleEntity)
}