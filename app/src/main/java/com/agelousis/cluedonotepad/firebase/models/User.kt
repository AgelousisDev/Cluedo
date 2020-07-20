package com.agelousis.cluedonotepad.firebase.models

import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val channel: String,
    val device: String,
    val character: Character
)