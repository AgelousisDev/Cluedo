package com.agelousis.cluedonotepad.main.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.databinding.NotepadRowColumnLayoutBinding
import com.agelousis.cluedonotepad.extensions.px
import com.agelousis.cluedonotepad.main.enums.ColumnState
import com.agelousis.cluedonotepad.main.enums.ColumnType
import com.agelousis.cluedonotepad.main.models.ColumnDataModel

class ColumnViewHolder(private val binding: NotepadRowColumnLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(columnDataModel: ColumnDataModel) {
        binding.columnDataModel = columnDataModel
        columnDataModel.columnType.takeIf { it == ColumnType.FIELD }?.let { setImageListeners() }
        columnDataModel.columnType.takeIf { it == ColumnType.HEADER_PLAYER }?.let {
            binding.rowColumnTextView.setTextColor(columnDataModel.color ?: 0)
        }
        columnDataModel.columnType.takeIf { it != ColumnType.ITEMS_TITLE && it != ColumnType.ITEM }?.let {
            binding.rowColumnConstraintLayout.layoutParams.also {
                it.width = 30.px
            }
        }
        binding.executePendingBindings()
    }

    private fun setImageListeners() {
        binding.rowColumnImageView.tag = binding.rowColumnImageView.tag ?: ColumnState.EMPTY
        itemView.setOnClickListener {
            binding.rowColumnImageView.tag = (binding.rowColumnImageView.tag as? ColumnState)?.nextState
            binding.rowColumnImageView.setImageResource((binding.rowColumnImageView.tag as? ColumnState)?.icon ?: 0)
        }
        itemView.setOnLongClickListener {
            binding.rowColumnImageView.setImageResource(R.drawable.ic_checkmark)
            true
        }
    }

}