package com.agelousis.cluedonotepad.dialog.presenters

import com.agelousis.cluedonotepad.splash.enumerations.Language

interface LanguagePresenter {
    fun onLanguageSelected(language: Language)
}