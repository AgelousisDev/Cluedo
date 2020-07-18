package com.agelousis.cluedonotepad.firebase.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val channelId: Int,
    val device: String
)