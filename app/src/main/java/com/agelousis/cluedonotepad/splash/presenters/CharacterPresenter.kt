package com.agelousis.cluedonotepad.splash.presenters

import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel

interface CharacterPresenter {
    fun onCharacterClicked(adapterPosition: Int)
    fun onCharacterSelected(adapterPosition: Int, characterRowModel: CharacterRowModel)
    fun onCharacterNameEntered(adapterPosition: Int, characterName: String)
}