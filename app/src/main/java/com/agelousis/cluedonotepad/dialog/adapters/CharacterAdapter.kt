package com.agelousis.cluedonotepad.dialog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.presenters.CharacterSelectPresenter
import com.agelousis.cluedonotepad.dialog.viewHolders.CharacterViewHolder
import com.agelousis.cluedonotepad.databinding.CharacterRowLayoutBinding

class CharacterAdapter(private val characterModelList: List<CharacterRowModel>, private val characterSelectPresenter: CharacterSelectPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterViewHolder(binding = CharacterRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = characterModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CharacterViewHolder)?.binding(characterRowModel = characterModelList.getOrNull(index = position) ?: return,
                characterSelectPresenter = characterSelectPresenter)
    }


}