package com.agelousis.cluedonotepad.splash.enumerations

import com.agelousis.cluedonotepad.R

enum class Language(val locale: String) {
    ENGLISH(locale = "en"),
    GREEK(locale = "el"),
    FRENCH(locale = "fr"),
    GERMAN(locale = "de"),
    ITALIAN(locale = "it"),
    CROATIAN(locale = "hr"),
    NORWEGIAN(locale = "no"),
    RUSSIAN(locale = "ru"),
    SPANISH(locale = "es");

    val icon
        get() = when(this) {
            ENGLISH -> R.drawable.ic_english_flag
            GREEK -> R.drawable.ic_greek_flag
            FRENCH -> R.drawable.ic_french_flag
            GERMAN -> R.drawable.ic_german_flag
            ITALIAN -> R.drawable.ic_italian_flag
            CROATIAN -> R.drawable.ic_croatian_flag
            NORWEGIAN -> R.drawable.ic_norwegian_flag
            RUSSIAN -> R.drawable.ic_russian_flag
            SPANISH -> R.drawable.ic_spanish_flag
        }

}