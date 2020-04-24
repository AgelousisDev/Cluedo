package com.agelousis.cluedonotepad.main.presenters

import com.agelousis.cluedonotepad.main.enums.ColumnState

interface ColumnPresenter {
    fun onIconSet(columnState: ColumnState, adapterPosition: Int)
}