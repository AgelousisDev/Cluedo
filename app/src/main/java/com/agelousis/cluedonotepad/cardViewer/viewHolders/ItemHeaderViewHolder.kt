package com.agelousis.cluedonotepad.cardViewer.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemHeaderPresenter
import com.agelousis.cluedonotepad.databinding.ItemHeaderRowLayoutBinding

class ItemHeaderViewHolder(private val binding: ItemHeaderRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(itemTitleModel: ItemTitleModel, presenter: ItemHeaderPresenter) {
        binding.itemTitleModel = itemTitleModel
        binding.presenter = presenter
        binding.executePendingBindings()
    }

}