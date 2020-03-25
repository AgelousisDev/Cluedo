package com.agelousis.cluedonotepad.main.models

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.main.enums.ColumnType

data class ColumnDataModel(val columnType: ColumnType, @DrawableRes val icon: Int? = null, val color: Int? = null, val title: String? = null,
                           @DrawableRes val customBackground: Int? = null) {

    val columnIconIsVisible: Boolean
        get() = when(columnType) {
            ColumnType.FIELD -> true
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