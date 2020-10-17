package com.agelousis.cluedonotepad.main.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.NotepadRowLayoutBinding
import com.agelousis.cluedonotepad.main.adapters.ColumnAdapter
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter

class RowViewHolder(private val binding: NotepadRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(rowDataModel: RowDataModel, columnPresenter: ColumnPresenter?) {
        binding.rowDataModel = rowDataModel
        configureRecyclerView(columnDataModelList = rowDataModel.columnDataModelList, columnPresenter = columnPresenter)
        binding.executePendingBindings()
    }

    private fun configureRecyclerView(columnDataModelList: List<ColumnDataModel>, columnPresenter: ColumnPresenter?) {
        binding.notepadColumnRecyclerView.adapter = ColumnAdapter(columnDataModelList = columnDataModelList, columnPresenter = columnPresenter)
    }

}