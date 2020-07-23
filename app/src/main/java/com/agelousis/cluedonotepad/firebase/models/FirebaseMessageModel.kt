package com.agelousis.cluedonotepad.firebase.models

import android.os.Parcelable
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FirebaseMessageModel(
    @SerializedName(value = "to") val firebaseToken: String,
    @SerializedName(value = "data") val firebaseMessageDataModel: FirebaseMessageDataModel
)

@Parcelize
data class FirebaseMessageDataModel(
    @SerializedName(value = "itemHeaderType")
    val itemHeaderType: ItemHeaderType,
    @SerializedName(value = "itemModel")
    val itemModel: ItemModel
): Parcelable