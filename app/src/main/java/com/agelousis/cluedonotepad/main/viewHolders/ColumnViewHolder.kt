package com.agelousis.cluedonotepad.main.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.databinding.NotepadRowColumnLayoutBinding
import com.agelousis.cluedonotepad.extensions.px
import com.agelousis.cluedonotepad.main.enums.ColumnState
import com.agelousis.cluedonotepad.main.enums.ColumnType
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter

class ColumnViewHolder(private val binding: NotepadRowColumnLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(columnDataModel: ColumnDataModel, columnPresenter: ColumnPresenter) {
        binding.columnDataModel = columnDataModel
        binding.rowColumnTextView.isSelected = true
        binding.rowColumnImageView.tag = columnDataModel.columnState ?: ColumnState.EMPTY
        columnDataModel.columnType.takeIf { it == ColumnType.FIELD }?.let { setImageListeners(columnPresenter = columnPresenter) }
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

    private fun setImageListeners(columnPresenter: ColumnPresenter) {
        (binding.rowColumnImageView.tag as? ColumnState)?.let {
            if (binding.rowColumnImageView.drawable == null)
                binding.rowColumnImageView.setImageResource(it.icon)
        }
        itemView.setOnClickListener {
            binding.rowColumnImageView.tag = (binding.rowColumnImageView.tag as? ColumnState)?.nextState
            columnPresenter.onIconSet(columnState = binding.rowColumnImageView.tag as? ColumnState ?: ColumnState.EMPTY, adapterPosition = adapterPosition)
            binding.rowColumnImageView.setImageResource((binding.rowColumnImageView.tag as? ColumnState)?.icon ?: 0)
        }
        itemView.setOnLongClickListener {
            columnPresenter.onIconSet(columnState = ColumnState.APPROVED, adapterPosition = adapterPosition)
            binding.rowColumnImageView.setImageResource(R.drawable.ic_checkmark)
            true
        }
    }

}