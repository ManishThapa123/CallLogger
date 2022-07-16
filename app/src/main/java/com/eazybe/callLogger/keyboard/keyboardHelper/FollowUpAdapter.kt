package com.eazybe.callLogger.keyboard.keyboardHelper

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.R
import com.eazybe.callLogger.api.models.entities.Data
import com.eazybe.callLogger.api.models.entities.SampleNotes
import com.eazybe.callLogger.api.models.responses.DataFollowUp
import com.eazybe.callLogger.databinding.FollowUpItemsBinding

class FollowUpAdapter(contextParent: Context): ListAdapter<DataFollowUp, FollowUpAdapter.ViewHolder>(object : DiffUtil.ItemCallback<DataFollowUp>(){

    override fun areItemsTheSame(oldItem: DataFollowUp, newItem: DataFollowUp): Boolean {
        return "${oldItem.id}" == "${newItem.id}"
    }

    override fun areContentsTheSame(oldItem: DataFollowUp, newItem: DataFollowUp): Boolean {
        return "${oldItem.id}" == "${newItem.id}"
    }

}) {

    private val context = contextParent

    inner class ViewHolder(val binding: FollowUpItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(notes: DataFollowUp, position: Int){
                binding.apply {
                    followUpText.text = notes.notes?.get(0)?.note
                    followUpDateTime.text = notes.notes?.get(0)?.date

                    when(position){
                        0 -> {
                            clFollowUp.setBackgroundColor(context.resources.getColor(R.color.purple_200))
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FollowUpItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes = getItem(position)
        holder.bind(notes, position)
    }
}