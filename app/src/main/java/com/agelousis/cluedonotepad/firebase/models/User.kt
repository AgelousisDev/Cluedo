package com.agelousis.cluedonotepad.firebase.models

import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val channel: String? = null,
    val device: String? = null,
    val character: Character? = null
)