package com.agelousis.cluedonotepad.splash.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterModel(val characterNameHint: String,
                          var characterName: String? = null,
                          var character: Int? = null,
                          @DrawableRes var characterIcon: Int = R.drawable.ic_person): Parcelable