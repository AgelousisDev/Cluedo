package com.agelousis.cluedonotepad.cards.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cards.EmptyModel
import com.agelousis.cluedonotepad.cards.enumerations.CardsAdapterViewType
import com.agelousis.cluedonotepad.cards.viewHolder.CardViewHolder
import com.agelousis.cluedonotepad.cards.viewHolder.EmptyViewHolder
import com.agelousis.cluedonotepad.databinding.CardRowLayoutBinding
import com.agelousis.cluedonotepad.databinding.EmptyRowLayoutBinding
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel

class CardsAdapter(private val list: List<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            CardsAdapterViewType.EMPTY_VIEW.type ->
                EmptyViewHolder(
                    binding = EmptyRowLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            CardsAdapterViewType.CARD_VIEW.type ->
                CardViewHolder(
                    binding = CardRowLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else ->
                EmptyViewHolder(
                    binding = EmptyRowLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? EmptyViewHolder)?.bind(
            emptyModel = list.getOrNull(
                index = position
            ) as? EmptyModel ?: return
        )
        (holder as? CardViewHolder)?.bind(
            firebaseMessageDataModel = list.getOrNull(
                index = position
            ) as? FirebaseMessageDataModel ?: return
        )
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        (list.getOrNull(index = position) as? EmptyModel)?.let {
            return CardsAdapterViewType.EMPTY_VIEW.type
        }
        (list.getOrNull(index = position) as? FirebaseMessageDataModel)?.let {
            return CardsAdapterViewType.CARD_VIEW.type
        }
        return super.getItemViewType(position)
    }

    fun reloadData() = notifyDataSetChanged()

}