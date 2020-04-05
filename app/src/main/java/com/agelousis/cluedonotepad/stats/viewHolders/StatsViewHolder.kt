package com.agelousis.cluedonotepad.stats.viewHolders

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.databinding.StatsRowLayoutBinding
import com.agelousis.cluedonotepad.stats.models.StatsModel
import com.agelousis.cluedonotepad.stats.presenters.ScorePresenter

class StatsViewHolder(private val binding: StatsRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(statsModel: StatsModel, scorePresenter: ScorePresenter) {
        binding.statsModel = statsModel
        binding.statsRowPlayerScoreField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                scorePresenter.onScoreChanged(adapterPosition = adapterPosition, score = s?.toString()?.toIntOrNull() ?: 0)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.executePendingBindings()
    }

}