package com.agelousis.cluedonotepad.main.enums

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.R

enum class ColumnState {
    APPROVED, LIGHT_APPROVED, CANCELLED, WARNING, EMPTY;

    val nextState: ColumnState
        get() = when(this) {
            EMPTY -> CANCELLED
            CANCELLED -> WARNING
            WARNING -> LIGHT_APPROVED
            else -> EMPTY
        }


    val icon: Int
        get() = when(this) {
            EMPTY -> 0
            CANCELLED -> R.drawable.ic_cancel
            LIGHT_APPROVED -> R.drawable.ic_lightbulb
            WARNING -> R.drawable.ic_warning
            APPROVED -> R.drawable.ic_checkmark
        }

}