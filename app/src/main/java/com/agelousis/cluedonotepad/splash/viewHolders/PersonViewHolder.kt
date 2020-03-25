package com.agelousis.cluedonotepad.splash.viewHolders

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.PersonRowLayoutBinding
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter

class PersonViewHolder(private val binding: PersonRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(characterModel: CharacterModel, characterPresenter: CharacterPresenter) {
        binding.position = adapterPosition
        binding.characterModel = characterModel
        binding.presenter = characterPresenter
        binding.characterNameField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                characterPresenter.onCharacterNameEntered(adapterPosition = adapterPosition, characterName = s?.toString() ?: return)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.executePendingBindings()
    }

}