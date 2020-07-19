package com.agelousis.cluedonotepad.cardViewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemsAdapterViewType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemHeaderPresenter
import com.agelousis.cluedonotepad.cardViewer.viewHolders.ItemHeaderViewHolder
import com.agelousis.cluedonotepad.cardViewer.viewHolders.ItemViewHolder
import com.agelousis.cluedonotepad.databinding.ItemHeaderRowLayoutBinding
import com.agelousis.cluedonotepad.databinding.ItemRowLayoutBinding

class ItemsAdapter(private val itemsList: List<Any>, private val presenter: ItemHeaderPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ItemsAdapterViewType.HEADER.type ->
                ItemHeaderViewHolder(
                    binding = ItemHeaderRowLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            ItemsAdapterViewType.ITEM.type ->
                ItemViewHolder(
                    binding = ItemRowLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            else ->
                ItemHeaderViewHolder(
                    binding = ItemHeaderRowLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemHeaderViewHolder)?.bind(
            itemTitleModel = itemsList.getOrNull(index = position) as? ItemTitleModel ?: return,
            presenter = presenter
        )
        (holder as? ItemViewHolder)?.bind(
            itemModel = itemsList.getOrNull(index = position) as? ItemModel ?: return
        )
    }

    override fun getItemViewType(position: Int): Int {
        (itemsList.getOrNull(index = position) as? ItemTitleModel)?.let {
            return ItemsAdapterViewType.HEADER.type
        }
        (itemsList.getOrNull(index = position) as? ItemModel)?.let {
            return ItemsAdapterViewType.ITEM.type
        }
        return super.getItemViewType(position)
    }

    fun reloadData() = notifyDataSetChanged()

}