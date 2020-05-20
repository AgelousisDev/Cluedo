package com.agelousis.cluedonotepad.dialog.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.LanguageRowLayoutBinding
import com.agelousis.cluedonotepad.dialog.models.LanguageModel
import com.agelousis.cluedonotepad.dialog.presenters.LanguagePresenter

class LanguageViewHolder(private val binding: LanguageRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(languageModel: LanguageModel, languagePresenter: LanguagePresenter?) {
        binding.languageModel = languageModel
        binding.presenter = languagePresenter
        binding.executePendingBindings()
    }

}