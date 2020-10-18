package com.agelousis.cluedonotepad.main.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.NotepadRowLayoutBinding
import com.agelousis.cluedonotepad.main.adapters.ColumnAdapter
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter

class RowViewHolder(private val binding: NotepadRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(rowDataModel: RowDataModel) {
        binding.rowDataModel = rowDataModel
        configureRecyclerView(columnDataModelList = rowDataModel.columnDataModelList)
        binding.executePendingBindings()
    }

    private fun configureRecyclerView(columnDataModelList: List<ColumnDataModel>) {
        binding.notepadColumnRecyclerView.adapter = ColumnAdapter(columnDataModelList = columnDataModelList)
    }

}