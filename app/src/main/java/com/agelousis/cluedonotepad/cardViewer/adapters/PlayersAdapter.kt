package com.agelousis.cluedonotepad.cardViewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.cardViewer.viewHolders.PlayerViewHolder
import com.agelousis.cluedonotepad.databinding.PlayerRowLayoutBinding
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class PlayersAdapter(private val playerList: List<CharacterModel>, private val presenter: PlayersPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlayerViewHolder(
            binding = PlayerRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = playerList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PlayerViewHolder)?.bind(
            characterModel = playerList.getOrNull(index = position) ?: return,
            presenter = presenter
        )
    }

    fun reloadData() = notifyDataSetChanged()

}