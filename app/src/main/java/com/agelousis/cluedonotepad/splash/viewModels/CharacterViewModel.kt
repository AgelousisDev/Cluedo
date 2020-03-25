package com.agelousis.cluedonotepad.splash.viewModels

import androidx.lifecycle.ViewModel
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class CharacterViewModel: ViewModel() {

    val characterArray = arrayListOf<CharacterModel>()

    fun addDefaultRow(characterModel: CharacterModel) {
        characterArray.add(characterModel)
    }

    fun addCharacter(characterModel: CharacterModel) {
        characterArray.add(characterModel)
    }

    fun removeCharacter() {
        characterArray.removeAt(characterArray.size -1)
    }


}