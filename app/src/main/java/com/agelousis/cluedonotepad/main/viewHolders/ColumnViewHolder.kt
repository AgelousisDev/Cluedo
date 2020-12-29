package com.agelousis.cluedonotepad.main.viewHolders

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.databinding.NotepadRowColumnLayoutBinding
import com.agelousis.cluedonotepad.extensions.px
import com.agelousis.cluedonotepad.extensions.setAnimatedImageResourceId
import com.agelousis.cluedonotepad.main.enums.ColumnState
import com.agelousis.cluedonotepad.main.enums.ColumnType
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter

class ColumnViewHolder(private val binding: NotepadRowColumnLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(columnDataModel: ColumnDataModel, columnPresenter: ColumnPresenter?) {
        binding.columnDataModel = columnDataModel
        binding.rowColumnTextView.isSelected = true
        columnDataModel.columnType.takeIf { it == ColumnType.FIELD }?.let {
            binding.rowColumnImageView.tag = columnDataModel.columnState ?: ColumnState.EMPTY
            setImageListeners(columnPresenter = columnPresenter)
        }
        binding.rowColumnConstraintLayout.updateLayoutParams<ViewGroup.LayoutParams> {
            height = 50.px
            width = columnDataModel.columnType.customWidth
        }
        columnDataModel.color?.let {
            binding.rowColumnTextView.setTextColor(it)
        }
        binding.executePendingBindings()
    }

    private fun setImageListeners(columnPresenter: ColumnPresenter?) {
        (binding.rowColumnImageView.tag as? ColumnState)?.let {
            if (binding.rowColumnImageView.drawable == null)
                binding.rowColumnImageView.setAnimatedImageResourceId(
                    resourceId = it.icon
                )
        }
        itemView.setOnClickListener {
            binding.rowColumnImageView.tag = (binding.rowColumnImageView.tag as? ColumnState)?.nextState
            binding.rowColumnImageView.setAnimatedImageResourceId(
                resourceId = (binding.rowColumnImageView.tag as? ColumnState)?.icon ?: 0
            )
            columnPresenter?.onIconSet(columnState = binding.rowColumnImageView.tag as? ColumnState ?: ColumnState.EMPTY, adapterPosition = adapterPosition)
        }
        itemView.setOnLongClickListener {
            binding.rowColumnImageView.tag = ColumnState.APPROVED
            binding.rowColumnImageView.setAnimatedImageResourceId(
                resourceId = R.drawable.ic_checkmark
            )
            columnPresenter?.onIconSet(columnState = ColumnState.APPROVED, adapterPosition = adapterPosition)
            true
        }
    }

}