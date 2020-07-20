package com.agelousis.cluedonotepad.cardViewer.models

import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.firebase.models.User

data class SelectedCardViewerModel(var user: User? = null,
                                   var itemHeaderType: ItemHeaderType? = null,
                                   var itemModel: ItemModel? = null
)