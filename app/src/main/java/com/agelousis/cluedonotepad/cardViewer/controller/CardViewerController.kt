package com.agelousis.cluedonotepad.cardViewer.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.extensions.firstOrNullWithType

object CardViewerController {

    fun getCards(context: Context, withPlayers: Boolean = false, withTools: Boolean = false, withRooms: Boolean = false, selectedItemModel: ItemModel? = null): ArrayList<Any> {
        val cards = ArrayList<Any>()
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_who_label),
            itemHeaderType = ItemHeaderType.WHO,
            icon = R.drawable.ic_person,
            isExpanded = withPlayers,
            background = R.drawable.item_row_background_top_left_right_corner
        ))
        if (withPlayers)
            context.resources.getStringArray(R.array.key_characters_array).forEachIndexed { index, value ->
                cards.add(
                    ItemModel(
                        item = value,
                        itemPosition = index,
                        itemHeaderType = ItemHeaderType.WHO,
                        isEnabled = value in MainApplication.currentSelectedCards
                    )
                )
            }
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_what_label),
            itemHeaderType = ItemHeaderType.WHAT,
            icon = R.drawable.ic_tool,
            isExpanded = withTools,
            background = R.drawable.item_row_background
        ))
        if (withTools)
            context.resources.getStringArray(R.array.key_tools_array).forEachIndexed { index, value ->
                cards.add(
                    ItemModel(
                        item = value,
                        itemPosition = index,
                        itemHeaderType = ItemHeaderType.WHAT,
                        isEnabled = value in MainApplication.currentSelectedCards
                    )
                )
            }
        cards.add(ItemTitleModel(
            title = context.resources.getString(R.string.key_where_label),
            itemHeaderType = ItemHeaderType.WHERE,
            icon = R.drawable.ic_room,
            isExpanded = withRooms,
            background = R.drawable.item_row_background_bottom_left_right_background
        ))
        if (withRooms)
            context.resources.getStringArray(R.array.key_rooms_array).forEachIndexed { index, value ->
                cards.add(
                    ItemModel(
                        item = value,
                        itemPosition = index,
                        itemHeaderType = ItemHeaderType.WHERE,
                        isEnabled = value in MainApplication.currentSelectedCards
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