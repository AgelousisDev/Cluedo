package com.agelousis.cluedonotepad.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agelousis.cluedonotepad.cards.interfaces.CardsUpdateListener
import com.agelousis.cluedonotepad.cards.adapter.CardsAdapter
import com.agelousis.cluedonotepad.databinding.CardsFragmentLayoutBinding
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class CardsFragment: Fragment(), CardsUpdateListener {

    override fun onUpdate() {
        (binding.cardsRecyclerView.adapter as? CardsAdapter)?.reloadData()
    }

    private lateinit var binding: CardsFragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NotePadActivity)?.cardsUpdateListener = this
        configureCardsRecyclerView()
    }

    private fun configureCardsRecyclerView() {
        binding.cardsRecyclerView.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        binding.cardsRecyclerView.adapter = CardsAdapter(
            list = (activity as? NotePadActivity)?.cardsSharedList ?: return
        )
    }

}