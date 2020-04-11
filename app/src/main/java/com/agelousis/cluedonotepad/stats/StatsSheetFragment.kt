package com.agelousis.cluedonotepad.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.splash.SplashActivity
import com.agelousis.cluedonotepad.stats.adapters.StatsAdapter
import com.agelousis.cluedonotepad.stats.models.StatsModel
import com.agelousis.cluedonotepad.stats.presenters.ScorePresenter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.stats_sheet_fragment_layout.*

class StatsSheetFragment: BottomSheetDialogFragment(), ScorePresenter {

    companion object {

        private const val STATS_MODEL_LIST_EXTRA = "StatsSheetFragment=statsModelListExtra"

        fun show(supportFragmentManager: FragmentManager, statsModelList: ArrayList<StatsModel>) {
            with(StatsSheetFragment()) {
                this.arguments = with(Bundle()) {
                    putParcelableArrayList(STATS_MODEL_LIST_EXTRA, statsModelList)
                    this
                }
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.STATS_SHEET_FRAGMENT)
        }
    }

    override fun onScoreChanged(adapterPosition: Int, score: Int) {
        statsModelList?.getOrNull(index = adapterPosition)?.playerScore = score
    }

    private val statsModelList by lazy { this.arguments?.getParcelableArrayList<StatsModel>(STATS_MODEL_LIST_EXTRA) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.stats_sheet_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        statsRecyclerView.layoutManager = flexLayoutManager
        statsRecyclerView.adapter = StatsAdapter(statsModelList = statsModelList ?: return, scorePresenter = this)
    }

    override fun onDestroy() {
        statsModelList?.let {
            (activity as? SplashActivity)?.statsModelList = it
        }
        super.onDestroy()
    }

}