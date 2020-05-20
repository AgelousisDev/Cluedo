package com.agelousis.cluedonotepad.dialog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.presenters.CharacterSelectPresenter
import com.agelousis.cluedonotepad.dialog.viewHolders.CharacterViewHolder
import com.agelousis.cluedonotepad.databinding.CharacterRowLayoutBinding
import com.agelousis.cluedonotepad.databinding.LanguageRowLayoutBinding
import com.agelousis.cluedonotepad.dialog.enumerations.BasicDialogSelectorType
import com.agelousis.cluedonotepad.dialog.models.LanguageModel
import com.agelousis.cluedonotepad.dialog.presenters.LanguagePresenter
import com.agelousis.cluedonotepad.dialog.viewHolders.LanguageViewHolder

class BasicDialogAdapter(private val list: List<Any>, private val characterSelectPresenter: CharacterSelectPresenter?, private val languagePresenter: LanguagePresenter?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            BasicDialogSelectorType.CHARACTER_VIEW_TYPE.value -> CharacterViewHolder(
                binding = CharacterRowLayoutBinding.inflate(inflater, parent, false)
            )
            BasicDialogSelectorType.LANGUAGE_VIEW_TYPE.value -> LanguageViewHolder(
                binding = LanguageRowLayoutBinding.inflate(inflater, parent, false)
            )
            else -> CharacterViewHolder(
                binding = CharacterRowLayoutBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? LanguageViewHolder)?.bind(
            languageModel = list.getOrNull(index = position) as? LanguageModel ?: return,
            languagePresenter = languagePresenter
        )
        (holder as? CharacterViewHolder)?.bind(
            characterRowModel = list.getOrNull(index = position) as? CharacterRowModel ?: return,
            characterSelectPresenter = characterSelectPresenter)
    }

    override fun getItemViewType(position: Int): Int {
        (list.getOrNull(index = position) as? LanguageModel)?.let { return BasicDialogSelectorType.LANGUAGE_VIEW_TYPE.value }
        (list.getOrNull(index = position) as? CharacterRowModel)?.let { return BasicDialogSelectorType.CHARACTER_VIEW_TYPE.value }
        return super.getItemViewType(position)
    }

}