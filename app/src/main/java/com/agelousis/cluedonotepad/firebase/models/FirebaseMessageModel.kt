package com.agelousis.cluedonotepad.firebase.models

import android.os.Parcelable
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FirebaseMessageModel(
    @SerializedName(value = "to") val firebaseToken: String,
    @SerializedName(value = "data") val firebaseMessageDataModel: FirebaseMessageDataModel
)

@Parcelize
data class FirebaseMessageDataModel(
    @SerializedName(value = "fromCharacter")
    val fromCharacter: CharacterModel?,
    @SerializedName(value = "itemHeaderType")
    val itemHeaderType: ItemHeaderType,
    @SerializedName(value = "itemModel")
    val itemModel: ItemModel
): Parcelable