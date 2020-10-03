package com.agelousis.cluedonotepad.main.models

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.main.enums.ColumnState
import com.agelousis.cluedonotepad.main.enums.ColumnType

data class ColumnDataModel(val columnType: ColumnType, @DrawableRes var icon: Int? = null, val color: Int? = null, val title: String? = null,
                           @DrawableRes val customBackground: Int? = null, var columnState: ColumnState? = null) {

    val columnIconIsVisible: Boolean
        get() = when(columnType) {
            ColumnType.FIELD, ColumnType.HEADER_PLAYER -> true
            else -> false
        }

    val columnTextIsVisible: Boolean
        get() = when(columnType) {
            ColumnType.HEADER_PLAYER, ColumnType.ITEMS_TITLE, ColumnType.ITEM, ColumnType.CUSTOM_TITLE -> true
            else -> false
        }

    val isTextBold: Boolean
        get() = when(columnType) {
            ColumnType.HEADER_PLAYER, ColumnType.ITEMS_TITLE, ColumnType.CUSTOM_TITLE -> true
            else -> false
        }

}