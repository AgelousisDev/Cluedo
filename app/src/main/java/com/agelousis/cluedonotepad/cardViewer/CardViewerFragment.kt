package com.agelousis.cluedonotepad.cardViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.cardViewer.adapters.ItemsAdapter
import com.agelousis.cluedonotepad.cardViewer.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.cardViewer.controller.CardViewerController
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.cardViewer.models.ItemTitleModel
import com.agelousis.cluedonotepad.cardViewer.models.SelectedCardViewerModel
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemHeaderPresenter
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemPresenter
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.databinding.CardViewerDialogFragmentLayoutBinding
import com.agelousis.cluedonotepad.extensions.forEachIfEach
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageModel
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class CardViewerFragment: Fragment(), PlayersPresenter, ItemHeaderPresenter, ItemPresenter {

    override fun onPlayerSelected(adapterPosition: Int, isSelected: Boolean) {
        characterModelList.forEach { it.playerIsSelected = false }
        characterModelList.getOrNull(index = adapterPosition)?.playerIsSelected = isSelected
        (binding?.playersRecyclerView?.adapter as? PlayersAdapter)?.reloadData()
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
        (binding?.itemsRecyclerView?.adapter as? ItemsAdapter)?.reloadData()
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
        (binding?.itemsRecyclerView?.adapter as? ItemsAdapter)?.reloadData()
        selectedCardViewerModel.itemModel = itemsList.getOrNull(index = adapterPosition) as? ItemModel
        sendButtonState = selectedCardViewerModel.itemHeaderType != null && selectedCardViewerModel.itemModel != null
    }

    private var binding: CardViewerDialogFragmentLayoutBinding? = null
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
        binding?.sendButton?.alpha = if (value) 1.0f else 0.5f
        binding?.sendButton?.isEnabled = value
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CardViewerDialogFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        configurePlayersRecyclerView()
        configureItemsRecyclerView()
    }

    private fun setupUI() {
        binding?.sendButton?.setOnClickListener {
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
        binding?.playersRecyclerView?.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        binding?.playersRecyclerView?.adapter = PlayersAdapter(
            playerList = characterModelList,
            presenter = this
        )
    }

    private fun configureItemsRecyclerView() {
        binding?.itemsRecyclerView?.layoutManager = GridLayoutManager(
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
        binding?.itemsRecyclerView?.adapter = ItemsAdapter(
            itemsList = itemsList,
            itemHeaderPresenter = this,
            itemPresenter = this
        )
    }

}