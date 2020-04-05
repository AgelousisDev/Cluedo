package com.agelousis.cluedonotepad.stats.presenters

interface ScorePresenter {
    fun onScoreChanged(adapterPosition: Int, score: Int)
}