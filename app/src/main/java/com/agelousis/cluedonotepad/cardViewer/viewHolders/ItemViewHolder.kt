package com.agelousis.cluedonotepad.cardViewer.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemPresenter
import com.agelousis.cluedonotepad.databinding.ItemRowLayoutBinding

class ItemViewHolder(private val binding: ItemRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(itemModel: ItemModel, presenter: ItemPresenter) {
        binding.itemModel = itemModel
        binding.itemModelChip.setOnClickListener {
            presenter.onItemSelected(adapterPosition = adapterPosition)
        }
        binding.executePendingBindings()
    }

}