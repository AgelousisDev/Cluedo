package com.agelousis.cluedonotepad.dialog.presenters

import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel

interface CharacterSelectPresenter {
    fun onCharacterSelected(characterRowModel: CharacterRowModel)
}