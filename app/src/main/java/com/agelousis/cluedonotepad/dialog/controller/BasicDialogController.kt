package com.agelousis.cluedonotepad.dialog.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.models.LanguageModel
import com.agelousis.cluedonotepad.splash.enumerations.Language

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

    val availableLanguages by lazy {
        listOf(
            LanguageModel(
                language = Language.ENGLISH
            ),
            LanguageModel(
                language = Language.GREEK
            ),
            LanguageModel(
                language = Language.FRENCH
            ),
            LanguageModel(
                language = Language.GERMAN
            ),
            LanguageModel(
                language = Language.ITALIAN
            ),
            LanguageModel(
                language = Language.CROATIAN
            ),
            LanguageModel(
                language = Language.NORWEGIAN
            )
        )
    }

}