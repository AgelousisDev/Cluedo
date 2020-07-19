package com.agelousis.cluedonotepad.cardViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.adapters.ItemsAdapter
import com.agelousis.cluedonotepad.cardViewer.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.cardViewer.controller.CardViewerController
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.presenters.ItemHeaderPresenter
import com.agelousis.cluedonotepad.cardViewer.presenters.PlayersPresenter
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.card_viewer_dialog_fragment_layout.*

class CardViewerBottomSheetFragment: BottomSheetDialogFragment(), PlayersPresenter, ItemHeaderPresenter {

    companion object {

        private const val CHARACTER_MODEL_LIST_EXTRA = "CardViewerBottomSheetFragment=characterModelListExtra"

        fun show(supportFragmentManager: FragmentManager, charactersModelList: ArrayList<CharacterModel>) {
            CardViewerBottomSheetFragment().also {
                it.arguments = with(Bundle()) {
                    putParcelableArrayList(CHARACTER_MODEL_LIST_EXTRA, charactersModelList)
                    this
                }
                it.retainInstance = true
            }.show(supportFragmentManager, Constants.CARD_VIEWER_SHEET_FRAGMENT)
        }
    }

    override fun onPlayerSelected(adapterPosition: Int) {
        characterModelList?.forEach { it.playerIsSelected = false }
        characterModelList?.getOrNull(index = adapterPosition)?.playerIsSelected = true
        (playersRecyclerView.adapter as? PlayersAdapter)?.reloadData()
    }

    override fun onItemHeaderSelected(itemHeaderType: ItemHeaderType) {
        itemsList.clear()
        when(itemHeaderType) {
            ItemHeaderType.WHO -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withPlayers = true
                ))
            }
            ItemHeaderType.WHAT -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withTools = true
                ))
            }
            ItemHeaderType.WHERE -> {
                itemsList.addAll(CardViewerController.getCards(
                    context = context ?: return,
                    withRooms = true
                ))
            }
        }
        itemsRecyclerView.scheduleLayoutAnimation()
        (itemsRecyclerView.adapter as? ItemsAdapter)?.reloadData()
    }

    private val characterModelList by lazy { arguments?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA) }
    private val itemsList by lazy {
        context?.let {
            CardViewerController.getCards(context = it)
        } ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.card_viewer_dialog_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurePlayersRecyclerView()
        configureItemsRecyclerView()
    }

    private fun configurePlayersRecyclerView() {
        playersRecyclerView.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        playersRecyclerView.adapter = PlayersAdapter(
            playerList = characterModelList ?: return,
            presenter = this
            )
    }

    private fun configureItemsRecyclerView() {
        itemsRecyclerView.adapter = ItemsAdapter(
            itemsList = itemsList,
            presenter = this
        )
    }

    override fun dismiss() {
        characterModelList?.forEach { it.playerIsSelected = false }
        super.dismiss()
    }

}