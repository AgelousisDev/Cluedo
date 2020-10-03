package com.agelousis.cluedonotepad.main.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.extensions.run
import com.agelousis.cluedonotepad.main.enums.ColumnType
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.models.RowDataModel
import com.agelousis.cluedonotepad.splash.models.CharacterModel

class NotePadController(private val context: Context) {

    private fun getCharacterColumns(characterModelList: List<CharacterModel>): ArrayList<ColumnDataModel> {
        val arrayList = arrayListOf(ColumnDataModel(columnType = ColumnType.ITEMS_TITLE, title = context.resources.getString(R.string.key_players_label), customBackground = R.drawable.item_row_background_left_top_corner),
        ColumnDataModel(columnType = ColumnType.CUSTOM_TITLE, title = context.resources.getString(R.string.key_final_label), customBackground = R.drawable.item_row_background))
        characterModelList.forEach { characterModel ->
            arrayList.add(
                ColumnDataModel(
                    columnType = ColumnType.HEADER_PLAYER,
                    icon = characterModel.characterIcon,
                    color = characterModel.character,
                    customBackground = android.R.color.transparent
                )
            )
        }
        return arrayList
    }

    private fun getItemTitleColumn(colorList: List<Int>, titleList: List<String>): RowDataModel {
        val arrayListOfColumns = arrayListOf<ColumnDataModel>()
        arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.EMPTY, customBackground = android.R.color.transparent))
        arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.EMPTY, customBackground = android.R.color.transparent))
        titleList.forEachIndexed { index, title ->
            arrayListOfColumns.add(
                ColumnDataModel(
                    columnType = ColumnType.CUSTOM_TITLE,
                    title = title,
                    customBackground = android.R.color.transparent,
                    color = colorList[index]
                )
            )
        }
        return RowDataModel(columnDataModelList = arrayListOfColumns)
    }

    private fun getItemColumnRows(arrayOfItems: Array<String>, size: Int): ArrayList<RowDataModel> {
        val arrayListOfRows = arrayListOf<RowDataModel>()
        arrayOfItems.forEachIndexed { index, title ->
            val customBackground = if (index % 2 == 0) R.color.whiteTwo else R.color.lightGrey
            val arrayListOfColumns = arrayListOf<ColumnDataModel>()
            arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.ITEM, title = title, customBackground = customBackground))
            (size + 1).run {
                arrayListOfColumns.add(ColumnDataModel(columnType = ColumnType.FIELD, customBackground = customBackground))
            }
            arrayListOfRows.add(RowDataModel(columnDataModelList = arrayListOfColumns))
        }
        return arrayListOfRows
    }

    fun getCluedoList(characterModelList: List<CharacterModel>, itemHeaderType: ItemHeaderType): ArrayList<RowDataModel> {
        val cluedoList = arrayListOf<RowDataModel>()
        cluedoList.add(RowDataModel(columnDataModelList = getCharacterColumns(characterModelList = characterModelList)))

        when(itemHeaderType) {
            ItemHeaderType.WHO -> {
                cluedoList.add(
                    getItemTitleColumn(
                        colorList = characterModelList.mapNotNull { it.character },
                        titleList = characterModelList.mapNotNull { it.characterName },
                    )
                )
                cluedoList.addAll(
                    getItemColumnRows(
                        arrayOfItems = context.resources.getStringArray(
                            R.array.key_characters_array
                        ), size = characterModelList.size
                    )
                )
            }
            ItemHeaderType.WHAT -> {
                cluedoList.add(
                    getItemTitleColumn(
                        colorList = characterModelList.mapNotNull { it.character },
                        titleList = characterModelList.mapNotNull { it.characterName },
                    )
                )
                cluedoList.addAll(
                    getItemColumnRows(
                        arrayOfItems = context.resources.getStringArray(
                            R.array.key_tools_array
                        ), size = characterModelList.size
                    )
                )
            }
            ItemHeaderType.WHERE -> {
                cluedoList.add(
                    getItemTitleColumn(
                        colorList = characterModelList.mapNotNull { it.character },
                        titleList = characterModelList.mapNotNull { it.characterName },
                    )
                )
                cluedoList.addAll(
                    getItemColumnRows(
                        arrayOfItems = context.resources.getStringArray(
                            R.array.key_rooms_array
                        ), size = characterModelList.size
                    )
                )
            }
        }

        return cluedoList
    }

}