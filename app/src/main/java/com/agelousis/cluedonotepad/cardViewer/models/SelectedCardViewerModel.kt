package com.agelousis.cluedonotepad.cardViewer.models

import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.splash.models.CharacterModel

data class SelectedCardViewerModel(var characterModel: CharacterModel? = null,
                                   var itemHeaderType: ItemHeaderType? = null,
                                   var itemModel: ItemModel? = null
)