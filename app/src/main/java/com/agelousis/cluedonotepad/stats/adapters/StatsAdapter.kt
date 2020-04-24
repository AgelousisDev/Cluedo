package com.agelousis.cluedonotepad.stats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.StatsRowLayoutBinding
import com.agelousis.cluedonotepad.stats.models.StatsModel
import com.agelousis.cluedonotepad.stats.presenters.ScorePresenter
import com.agelousis.cluedonotepad.stats.viewHolders.StatsViewHolder

class StatsAdapter(private val statsModelList: List<StatsModel>, private val scorePresenter: ScorePresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StatsViewHolder(binding = StatsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = statsModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? StatsViewHolder)?.bind(statsModel = statsModelList.getOrNull(index = position) ?: return, scorePresenter = scorePresenter)
    }

}