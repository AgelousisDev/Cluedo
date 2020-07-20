package com.agelousis.cluedonotepad.splash.models

import android.os.Parcelable
import com.agelousis.cluedonotepad.splash.enumerations.GameType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameTypeModel(val gameType: GameType,
                         val channel: String? = null
): Parcelable