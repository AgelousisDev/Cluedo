package com.agelousis.cluedonotepad.main.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.extensions.run
import com.agelousis.cluedonotepad.main.enums.ColumnType
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class NotePadController(private val context: Context) {

    private fun getCharacterColumns(characterModelList: List<CharacterModel>): ArrayList<ColumnDataModel> {
        val arrayList = arrayListOf(ColumnDataModel(columnType = ColumnType.ITEMS_TITLE, title = context.resources.getString(R.string.key_players_label), customBackground = R.drawable.item_row_background),
        ColumnDataModel(columnType = ColumnType.CUSTOM_TITLE, title = context.resources.getString(R.string.key_final_label), customBackground = R.drawable.custom_title_dash_circle_background))
        characterModelList.forEach {
            arrayList.add(ColumnDataModel(columnType = ColumnType.HEADER_PLAYER, icon = R.drawable.ic_person, color = it.character, title = it.characterName, customBackground = android.R.color.transparent))
        }
        return arrayList
    }

    private fun getItemTitleColumn(title: String, size: Int): RowDataModel {
        val arrayListOfColumns = arrayListOf<ColumnDataModel>()
        arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.ITEMS_TITLE, title = title, customBackground = android.R.color.transparent))
        (size + 1).run {
            arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.EMPTY, customBackground = R.drawable.empty_row_background))
        }
        return RowDataModel(columnDataModelList = arrayListOfColumns)
    }

    private fun getItemColumnRows(arrayOfItems: Array<String>, size: Int): ArrayList<RowDataModel> {
        val arrayListOfRows = arrayListOf<RowDataModel>()
        arrayOfItems.forEach {
            val arrayListOfColumns = arrayListOf<ColumnDataModel>()
            arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.ITEM, title = it, customBackground = R.drawable.item_row_background))
            (size + 1).run {
                arrayListOfColumns.add(ColumnDataModel(ColumnType.FIELD))
            }
            arrayListOfRows.add(RowDataModel(columnDataModelList = arrayListOfColumns))
        }
        return arrayListOfRows
    }

    fun getCluedoList(characterModelList: List<CharacterModel>): ArrayList<RowDataModel> {
        val cluedoList = arrayListOf<RowDataModel>()
        cluedoList.add(RowDataModel(columnDataModelList = getCharacterColumns(characterModelList = characterModelList)))

        cluedoList.add(getItemTitleColumn(title = context.resources.getString(R.string.key_who_label), size = characterModelList.size))
        cluedoList.addAll(getItemColumnRows(arrayOfItems = context.resources.getStringArray(R.array.key_characters_array), size = characterModelList.size))

        cluedoList.add(getItemTitleColumn(title = context.resources.getString(R.string.key_what_label), size = characterModelList.size))
        cluedoList.addAll(getItemColumnRows(arrayOfItems = context.resources.getStringArray(R.array.key_tools_array), size = characterModelList.size))

        cluedoList.add(getItemTitleColumn(title = context.resources.getString(R.string.key_where_label), size = characterModelList.size))
        cluedoList.addAll(getItemColumnRows(arrayOfItems = context.resources.getStringArray(R.array.key_rooms_array), size = characterModelList.size))

        return cluedoList
    }

}