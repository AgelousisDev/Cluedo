package com.agelousis.cluedonotepad.cards.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.CardRowLayoutBinding
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel

class CardViewHolder(private val binding: CardRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(firebaseMessageDataModel: FirebaseMessageDataModel) {
        binding.firebaseMessageDataModel = firebaseMessageDataModel
        binding.executePendingBindings()
    }

}