package com.eazybe.callLogger.keyboard.keyboardHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eazybe.callLogger.api.models.entities.SampleNotes
import com.eazybe.callLogger.databinding.FollowUpItemsBinding

class FollowUpAdapter(): ListAdapter<SampleNotes, FollowUpAdapter.ViewHolder>(object : DiffUtil.ItemCallback<SampleNotes>(){
    override fun areItemsTheSame(oldItem: SampleNotes, newItem: SampleNotes): Boolean {
        return "${oldItem.notes}" == "${newItem.notes}"
    }

    override fun areContentsTheSame(oldItem: SampleNotes, newItem: SampleNotes): Boolean {
        return "${oldItem.notes}" == "${newItem.notes}"
    }

}) {

    inner class ViewHolder(val binding: FollowUpItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(notes: SampleNotes, position: Int){
                binding.apply {
                    followUpText.text = notes.notes
                    followUpDateTime.text = notes.dateTime
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