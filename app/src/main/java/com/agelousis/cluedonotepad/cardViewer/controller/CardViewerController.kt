package com.agelousis.cluedonotepad.cardViewer.controller

import android.content.Context
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel

object CardViewerController {

    fun getCards(context: Context, withPlayers: Boolean = false, withTools: Boolean = false, withRooms: Boolean = false): ArrayList<Any> {
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
                        item = it
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
                        item = it
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
                        item = it
                    )
                )
            }
        return cards
    }

}