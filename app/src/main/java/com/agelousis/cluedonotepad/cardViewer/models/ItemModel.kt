package com.agelousis.cluedonotepad.cardViewer.models

import android.os.Parcelable
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(val item: String, var isSelected: Boolean = false, val itemHeaderType: ItemHeaderType): Parcelable