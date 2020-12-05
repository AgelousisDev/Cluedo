package com.agelousis.cluedonotepad.cardViewer.models

import android.os.Parcelable
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemModel(val item: String, val itemPosition: Int, var isSelected: Boolean = false, val itemHeaderType: ItemHeaderType, val isEnabled: Boolean = false): Parcelable