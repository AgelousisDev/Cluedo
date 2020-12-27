package com.agelousis.cluedonotepad.main.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.NotepadRowLayoutBinding
import com.agelousis.cluedonotepad.main.adapters.ColumnAdapter
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.main.presenters.RowPresenter

class RowViewHolder(private val binding: NotepadRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(rowDataModel: RowDataModel, presenter: RowPresenter) {
        binding.rowDataModel = rowDataModel
        configureRecyclerView(columnDataModelList = rowDataModel.columnDataModelList, presenter = presenter)
        binding.executePendingBindings()
    }

    private fun configureRecyclerView(columnDataModelList: List<ColumnDataModel>, presenter: RowPresenter) {
        binding.notepadColumnRecyclerView.adapter = ColumnAdapter(columnDataModelList = columnDataModelList)
        binding.notepadColumnRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.removeOnScrollListener(this)
                presenter.onRowScrolled(
                    scrollX = dx
                )
                recyclerView.addOnScrollListener(this)
            }

        })
    }

}