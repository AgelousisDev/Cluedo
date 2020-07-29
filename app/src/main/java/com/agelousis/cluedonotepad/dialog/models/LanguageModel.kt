package com.agelousis.cluedonotepad.dialog.models

import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.splash.enumerations.Language

data class LanguageModel(@DrawableRes val languageIcon: Int, val language: Language)