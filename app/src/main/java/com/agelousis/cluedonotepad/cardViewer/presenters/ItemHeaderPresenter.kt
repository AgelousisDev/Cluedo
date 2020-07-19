package com.agelousis.cluedonotepad.cardViewer.presenters

import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType

interface ItemHeaderPresenter {
    fun onItemHeaderSelected(itemHeaderType: ItemHeaderType)
}