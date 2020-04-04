package com.agelousis.cluedonotepad.dialog.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel

object BasicDialogController {

    fun getCluedoCharacters(context: Context): List<CharacterRowModel> {
        val characterModelList = arrayListOf<CharacterRowModel>()
        val characterIconsArray = context.resources.obtainTypedArray(R.array.key_character_icons_array)
        context.resources.getStringArray(R.array.key_characters_array).forEachIndexed { index, characterName ->
            characterModelList.add(CharacterRowModel(characterIcon = characterIconsArray.getResourceId(index, -1),
                characterName = characterName, characterColor = context.resources.getIntArray(R.array.key_characters_colors_array)[index]))
        }
        characterIconsArray.recycle()
        return characterModelList
    }

}