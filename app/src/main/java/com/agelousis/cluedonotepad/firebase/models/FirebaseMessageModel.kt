package com.agelousis.cluedonotepad.firebase.models

import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.google.gson.annotations.SerializedName

data class FirebaseMessageModel(
    @SerializedName(value = "to") val firebaseToken: String,
    @SerializedName(value = "data") val firebaseMessageDataModel: FirebaseMessageDataModel
)

data class FirebaseMessageDataModel(
    val itemHeaderType: ItemHeaderType,
    val itemModel: ItemModel
)