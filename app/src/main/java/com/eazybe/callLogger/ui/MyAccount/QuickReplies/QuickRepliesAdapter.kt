package com.eazybe.callLogger.ui.MyAccount.QuickReplies

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.R
import com.eazybe.callLogger.api.models.responses.QuickReplies.UserQuickRepliesMessage
import com.eazybe.callLogger.databinding.ItemQuickReplyBinding

abstract class QuickRepliesAdapter(val onProfileClicked: (clickedUser: UserQuickRepliesMessage) -> Unit) :
    ListAdapter<UserQuickRepliesMessage, QuickRepliesAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<UserQuickRepliesMessage>() {
        override fun areItemsTheSame(
            oldItem: UserQuickRepliesMessage,
            newItem: UserQuickRepliesMessage
        ): Boolean {
            return "${oldItem.id}" == "${newItem.id}"
        }

        override fun areContentsTheSame(
            oldItem: UserQuickRepliesMessage,
            newItem: UserQuickRepliesMessage
        ): Boolean {
            return "${oldItem.id}" == "${newItem.id}"
        }

    }) {

    private var context: Context? = null
    inner class ViewHolder(val binding: ItemQuickReplyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserQuickRepliesMessage) {
            context = getContext()
            binding.apply {
                txtTitle.text = data.quickReplyTitle
                quickReplyBody.text = data.quickReplyMessage
                btnSend.icon = ContextCompat.getDrawable(context!!,R.drawable.ic_whatsapp)
                btnSend.iconTint = ColorStateList.valueOf(ContextCompat.getColor(context!!,R.color.green_light))

                if (!data.fileName.isNullOrEmpty()) {
                    txtAttachment.text = data.fileName
                    lLAttachment.visibility = View.VISIBLE
                    when (data.fileName?.takeLast(4)?.lowercase()){
                        ".pdf" -> {
                            imgAttatchment.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_file_pdf
                                )
                            )
                        }
                        ".mp3" ->{

                            imgAttatchment.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_file_mp3
                                )
                            )
                        }
                        ".mp4" ->{
                            imgAttatchment.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_file_mp4
                                )
                            )
                        }
                        ".png", ".jpg", "jpeg", "webp" ->{

                            imgAttatchment.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_file_jpg
                                )
                            )
                        }
                        else ->{
                            imgAttatchment.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.ic_file_zip
                                )
                            )

                        }
                    }
                } else {
                    lLAttachment.visibility = View.GONE
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuickReplyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userQuickReply = getItem(position)
        holder.bind(userQuickReply)
        holder.binding.root.setOnClickListener {
            onProfileClicked(userQuickReply)
        }
        setActionButtonClickListeners(holder.binding, userQuickReply)
    }

    private fun setActionButtonClickListeners(
        binding: ItemQuickReplyBinding,
        userQuickReply: UserQuickRepliesMessage
    ) {
        binding.btnSend.setOnClickListener {
            sendQuickReplyToWhatsApp(userQuickReply)
        }
    }
    abstract fun sendQuickReplyToWhatsApp(userQuickReply: UserQuickRepliesMessage)
    abstract fun getContext(): Context
}