package com.agelousis.cluedonotepad.dialog.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.presenters.CharacterSelectPresenter
import com.agelousis.cluedonotepad.databinding.CharacterRowLayoutBinding

class CharacterViewHolder(private val binding: CharacterRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun binding(characterRowModel: CharacterRowModel, characterSelectPresenter: CharacterSelectPresenter) {
        binding.characterModel = characterRowModel
        binding.presenter = characterSelectPresenter
        binding.executePendingBindings()
    }

}