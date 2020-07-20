package com.agelousis.cluedonotepad.dialog.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.models.LanguageModel

object BasicDialogController {

    fun getCluedoCharacters(context: Context): List<CharacterRowModel> {
        val characterModelList = arrayListOf<CharacterRowModel>()
        val characterIconsArray = context.resources.obtainTypedArray(R.array.key_character_icons_array)
        context.resources.getStringArray(R.array.key_characters_array).forEachIndexed { index, characterName ->
            characterModelList.add(
                CharacterRowModel(
                    characterIcon = characterIconsArray.getResourceId(index, -1),
                    characterName = characterName,
                    characterColor = context.resources.getIntArray(R.array.key_characters_colors_array)[index],
                    character = Character.values()[index]
                )
            )
        }
        characterIconsArray.recycle()
        return characterModelList
    }

    fun getAvailableLanguages() = listOf(
        LanguageModel(
            languageIcon = R.drawable.ic_english_flag,
            languageCode = Constants.ENGLISH_LANGUAGE_CODE
        ),
        LanguageModel(
            languageIcon = R.drawable.ic_greek_flag,
            languageCode = Constants.GREEK_LANGUAGE_CODE
        )
    )

}