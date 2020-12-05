package com.agelousis.cluedonotepad.stats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatsModel(var playerName: String, var playerColor: Int, var playerScore: Int): Parcelable