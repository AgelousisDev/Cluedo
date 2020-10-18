package com.agelousis.cluedonotepad.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.NotepadRowLayoutBinding
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter
import com.agelousis.cluedonotepad.main.viewHolders.RowViewHolder

class RowAdapter(private val rowDataModelList: List<RowDataModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RowViewHolder(binding = NotepadRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = rowDataModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? RowViewHolder)?.bind(
            rowDataModel = rowDataModelList.getOrNull(index = position) ?: return
        )
    }

}