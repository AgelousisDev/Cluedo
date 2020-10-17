package com.agelousis.cluedonotepad.cardViewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.cardViewer.adapters.ItemsAdapter
import com.agelousis.cluedonotepad.cardViewer.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.cardViewer.controller.CardViewerController
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.interfaces.CardViewerListener
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.cardViewer.models.SelectedCardViewerModel
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemHeaderPresenter
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemPresenter
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.extensions.forEachIfEach
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageModel
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.card_viewer_dialog_fragment_layout.*

class CardViewerFragment: Fragment(R.layout.card_viewer_dialog_fragment_layout), PlayersPresenter, ItemHeaderPresenter, ItemPresenter, CardViewerListener {

    override fun onUpdate() {
        (itemsRecyclerView.adapter as? ItemsAdapter)?.reloadData()
    }

    override fun onPlayerSelected(adapterPosition: Int, isSelected: Boolean) {
        characterModelList.forEach { it.playerIsSelected = false }
        characterModelList.getOrNull(index = adapterPosition)?.playerIsSelected = isSelected
        (playersRecyclerView.adapter as? PlayersAdapter)?.reloadData()
        selectedCardViewerModel.characterModel = if (isSelected) characterModelList.getOrNull(index = adapterPosition) else null
    }

    override fun onItemHeaderSelected(itemTitleModel: ItemTitleModel) {
        itemsList.clear()
        when(itemTitleModel.itemHeaderType) {
            ItemHeaderType.WHO -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withPlayers = !itemTitleModel.isExpanded,
                    selectedItemModel = selectedCardViewerModel.itemModel
                ))
            }
            ItemHeaderType.WHAT -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withTools = !itemTitleModel.isExpanded,
                    selectedItemModel = selectedCardViewerModel.itemModel
                ))
            }
            ItemHeaderType.WHERE -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withRooms = !itemTitleModel.isExpanded,
                    selectedItemModel = selectedCardViewerModel.itemModel
                ))
            }
        }
        (itemsRecyclerView.adapter as? ItemsAdapter)?.reloadData()
        selectedCardViewerModel.itemHeaderType = itemTitleModel.itemHeaderType
        sendButtonState = selectedCardViewerModel.itemHeaderType != null && selectedCardViewerModel.itemModel != null
    }

    override fun onItemSelected(adapterPosition: Int) {
        itemsList.forEachIfEach(
            predicate = {
                it is ItemModel
            }
        ) {
            (it as ItemModel).isSelected = false
        }
        (itemsList.getOrNull(index = adapterPosition) as? ItemModel)?.isSelected = (itemsList.getOrNull(index = adapterPosition) as? ItemModel)?.isSelected == false
        (itemsRecyclerView.adapter as? ItemsAdapter)?.reloadData()
        selectedCardViewerModel.itemModel = itemsList.getOrNull(index = adapterPosition) as? ItemModel
        sendButtonState = selectedCardViewerModel.itemHeaderType != null && selectedCardViewerModel.itemModel != null
    }

    private val characterModelList by lazy {
        ArrayList((activity as? NotePadActivity)?.characterModelArray?.drop(n = 1) ?: listOf())
    }
    private val itemsList by lazy {
        context?.let {
            CardViewerController.getCards(context = it)
        } ?: arrayListOf()
    }
    private val selectedCardViewerModel by lazy { SelectedCardViewerModel() }
    private var sendButtonState = false
    set(value) {
        field = value
        sendButton.alpha = if (value) 1.0f else 0.5f
        sendButton.isEnabled = value
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NotePadActivity)?.cardViewerListener = this
        setupUI()
        configurePlayersRecyclerView()
        configureItemsRecyclerView()
    }

    private fun setupUI() {
        sendButton.setOnClickListener {
            (activity as? NotePadActivity)?.initializeUsers(
                character = selectedCardViewerModel.characterModel?.characterEnum
            ) inner@ { users ->
                users.filterNot { it?.device == MainApplication.firebaseToken }.mapNotNull { it?.device }.forEach { device ->
                    (activity as? NotePadActivity)?.viewModel?.sendFirebaseToken(
                        firebaseMessageModel = FirebaseMessageModel(
                            firebaseToken = device,
                            firebaseMessageDataModel = FirebaseMessageDataModel(
                                itemHeaderType = selectedCardViewerModel.itemHeaderType ?: return@inner,
                                itemModel = selectedCardViewerModel.itemModel ?: return@inner
                            )
                        )
                    )
                }
            }
        }
    }

    private fun configurePlayersRecyclerView() {
        playersRecyclerView.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        playersRecyclerView.adapter = PlayersAdapter(
            playerList = characterModelList,
            presenter = this
        )
    }

    private fun configureItemsRecyclerView() {
        itemsRecyclerView.layoutManager = GridLayoutManager(
            context ?: return,
            2
        ).also {
            it.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when(itemsList.getOrNull(index = position)) {
                        is ItemTitleModel -> 2
                        is ItemModel -> 1
                        else -> 2
                    }
            }
        }
        itemsRecyclerView.adapter = ItemsAdapter(
            itemsList = itemsList,
            itemHeaderPresenter = this,
            itemPresenter = this
        )
    }

}