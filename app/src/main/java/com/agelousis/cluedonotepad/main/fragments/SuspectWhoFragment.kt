package com.agelousis.cluedonotepad.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.databinding.SuspectFragmentLayoutBinding
import com.agelousis.cluedonotepad.extensions.hasNotch
import com.agelousis.cluedonotepad.extensions.px
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.main.adapters.RowAdapter
import com.agelousis.cluedonotepad.main.controller.NotePadController

class SuspectWhoFragment: Fragment() {

    private var binding: SuspectFragmentLayoutBinding? = null
    private val controller by lazy {
        context?.let { NotePadController(context = it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SuspectFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        configureRecyclerView()
    }

    private fun setupUI() {
        if (activity?.window?.hasNotch == true)
            binding?.constraintLayout?.setPadding(40.px, 0, 0, 0)
    }

    private fun configureRecyclerView() {
        binding?.notePadRecyclerView?.adapter = RowAdapter(
            rowDataModelList = controller?.getCluedoList(
                characterModelList = (activity as? NotePadActivity)?.characterModelArray ?: return,
                itemHeaderType = ItemHeaderType.WHO
            ) ?: return
        )
    }

}