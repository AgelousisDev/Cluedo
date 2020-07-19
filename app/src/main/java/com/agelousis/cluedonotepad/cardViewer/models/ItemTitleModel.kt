package com.agelousis.cluedonotepad.cardViewer.models

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType

data class ItemTitleModel(val title: String,
                          val itemHeaderType: ItemHeaderType,
                          @DrawableRes val icon: Int,
                          val isExpanded: Boolean
)