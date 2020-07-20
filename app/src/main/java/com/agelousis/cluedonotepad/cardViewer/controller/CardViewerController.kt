package com.agelousis.cluedonotepad.cardViewer.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.extensions.firstOrNullWithType
import com.agelousis.cluedonotepad.extensions.forEachIfEach

object CardViewerController {

    fun getCards(context: Context, withPlayers: Boolean = false, withTools: Boolean = false, withRooms: Boolean = false, selectedItemModel: ItemModel? = null): ArrayList<Any> {
        val cards = ArrayList<Any>()
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_who_label),
            itemHeaderType = ItemHeaderType.WHO,
            icon = R.drawable.ic_person,
            isExpanded = withPlayers
        ))
        if (withPlayers)
            context.resources.getStringArray(R.array.key_characters_array).forEach {
                cards.add(
                    ItemModel(
                        item = it,
                        itemHeaderType = ItemHeaderType.WHO
                    )
                )
            }
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_what_label),
            itemHeaderType = ItemHeaderType.WHAT,
            icon = R.drawable.ic_tool,
            isExpanded = withTools
        ))
        if (withTools)
            context.resources.getStringArray(R.array.key_tools_array).forEach {
                cards.add(
                    ItemModel(
                        item = it,
                        itemHeaderType = ItemHeaderType.WHAT
                    )
                )
            }
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_where_label),
            itemHeaderType = ItemHeaderType.WHERE,
            icon = R.drawable.ic_room,
            isExpanded = withRooms
        ))
        if (withRooms)
            context.resources.getStringArray(R.array.key_rooms_array).forEach {
                cards.add(
                    ItemModel(
                        item = it,
                        itemHeaderType = ItemHeaderType.WHERE
                    )
                )
            }
        selectedItemModel?.let { itemModel ->
            cards.firstOrNullWithType(
                typeBlock = {
                    it as? ItemModel
                },
                predicate = {
                    it?.item == itemModel.item
                }
            )?.isSelected = true
        }
        return cards
    }

}