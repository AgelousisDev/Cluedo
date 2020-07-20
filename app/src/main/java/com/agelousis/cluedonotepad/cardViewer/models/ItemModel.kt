package com.agelousis.cluedonotepad.cardViewer.models

import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType

data class ItemModel(val item: String, var isSelected: Boolean = false, val itemHeaderType: ItemHeaderType)