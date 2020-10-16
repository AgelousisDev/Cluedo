package com.agelousis.cluedonotepad.cards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cards.interfaces.CardsUpdateListener
import com.agelousis.cluedonotepad.cards.adapter.CardsAdapter
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.cards_fragment_layout.*

class CardsFragment: Fragment(R.layout.cards_fragment_layout), CardsUpdateListener {

    override fun onUpdate() {
        (cardsRecyclerView.adapter as? CardsAdapter)?.reloadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NotePadActivity)?.cardsUpdateListener = this
        configureCardsRecyclerView()
    }

    private fun configureCardsRecyclerView() {
        cardsRecyclerView.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        cardsRecyclerView.adapter = CardsAdapter(
            list = (activity as? NotePadActivity)?.cardsSharedList ?: return
        )
    }

}