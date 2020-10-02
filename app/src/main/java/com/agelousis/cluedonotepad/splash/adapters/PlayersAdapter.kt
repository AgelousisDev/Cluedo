package com.agelousis.cluedonotepad.splash.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.databinding.PersonRowLayoutBinding
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.extensions.showCharacterOptions
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter
import com.agelousis.cluedonotepad.splash.viewHolders.PersonViewHolder

class PlayersAdapter(private val context: Context, private val characterListModel: List<CharacterModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), CharacterPresenter {

    override fun onCharacterClicked(adapterPosition: Int) {
        context.showCharacterOptions(title = context.getString(R.string.key_choose_character_title), adapterPosition = adapterPosition, characterPresenter = this)
    }

    override fun onCharacterSelected(adapterPosition: Int, characterRowModel: CharacterRowModel) {
        characterListModel.getOrNull(index = adapterPosition)?.character = characterRowModel.characterColor
        characterListModel.getOrNull(index = adapterPosition)?.characterIcon = characterRowModel.characterIcon
        characterListModel.getOrNull(index = adapterPosition)?.characterEnum = characterRowModel.character
        notifyDataSetChanged()
    }

    override fun onCharacterNameEntered(adapterPosition: Int, characterName: String) {
        characterListModel.getOrNull(index = adapterPosition)?.characterName = characterName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonViewHolder(
        binding = PersonRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = characterListModel.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PersonViewHolder)?.bind(characterModel = characterListModel.getOrNull(position) ?: return,
            characterPresenter = this)
    }


    private fun removeLastPosition() {
        notifyItemRemoved(itemCount)
        notifyItemRangeChanged(itemCount, itemCount)
    }

    fun update(appendState: Boolean) {
        when(appendState) {
            true -> notifyItemInserted(itemCount)
            false -> {
                removeLastPosition()
                //notifyItemInserted(itemCount)
            }
        }
    }

}