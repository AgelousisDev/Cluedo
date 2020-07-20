package com.agelousis.cluedonotepad.dialog.models

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.dialog.enumerations.Character

data class CharacterRowModel(@DrawableRes val characterIcon: Int, val characterName: String, val characterColor: Int, val character: Character)