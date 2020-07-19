package com.agelousis.cluedonotepad.cardViewer.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.databinding.PlayerRowLayoutBinding
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class PlayerViewHolder(private val binding: PlayerRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(characterModel: CharacterModel, presenter: PlayersPresenter) {
        binding.characterModel = characterModel
        binding.playersView.setOnClickListener {
            presenter.onPlayerSelected(
                adapterPosition = adapterPosition
            )
        }
        binding.playersView.alpha = if (characterModel.playerIsSelected) 0.5f else 1.0f
        binding.executePendingBindings()
    }

}