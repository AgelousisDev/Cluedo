package com.agelousis.cluedonotepad.cardViewer.presenters

import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel

interface ItemHeaderPresenter {
    fun onItemHeaderSelected(itemTitleModel: ItemTitleModel)
}