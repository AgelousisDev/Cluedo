package com.agelousis.cluedonotepad.cards.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cards.EmptyModel
import com.agelousis.cluedonotepad.databinding.EmptyRowLayoutBinding

class EmptyViewHolder(private val binding: EmptyRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(emptyModel: EmptyModel) {
        binding.emptyModel = emptyModel
        binding.executePendingBindings()
    }

}