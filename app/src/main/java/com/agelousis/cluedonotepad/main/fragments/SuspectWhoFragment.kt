package com.agelousis.cluedonotepad.main.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.main.adapters.RowAdapter
import com.agelousis.cluedonotepad.main.controller.NotePadController
import kotlinx.android.synthetic.main.suspect_fragment_layout.*

class SuspectWhoFragment: Fragment(R.layout.suspect_fragment_layout) {

    private val controller by lazy {
        context?.let { NotePadController(context = it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        notePadRecyclerView.adapter = RowAdapter(
            rowDataModelList = controller?.getCluedoList(
                characterModelList = (activity as? NotePadActivity)?.characterModelArray ?: return,
                itemHeaderType = ItemHeaderType.WHO
            ) ?: return,
            columnPresenter = activity as? NotePadActivity
        )
    }

}