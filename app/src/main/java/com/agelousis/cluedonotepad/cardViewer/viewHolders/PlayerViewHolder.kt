package com.agelousis.cluedonotepad.cardViewer.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.databinding.PlayerRowLayoutBinding
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class PlayerViewHolder(private val binding: PlayerRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(characterModel: CharacterModel, presenter: PlayersPresenter) {
        binding.characterModel = characterModel
        binding.playersView.tag = binding.playersView.tag ?: false
        binding.playersView.setOnClickListener {
            it.tag = !(it.tag as Boolean)
            presenter.onPlayerSelected(
                adapterPosition = adapterPosition,
                isSelected = it.tag as Boolean
            )
        }
        binding.executePendingBindings()
    }

}