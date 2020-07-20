package com.agelousis.cluedonotepad.main

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.cardViewer.CardViewerBottomSheetFragment
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.main.adapters.RowAdapter
import com.agelousis.cluedonotepad.main.controller.NotePadController
import com.agelousis.cluedonotepad.main.timer.TimerHelper
import com.agelousis.cluedonotepad.main.timer.TimerListener
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.models.GameTypeModel
import kotlinx.android.synthetic.main.activity_notepad.*

class NotePadActivity : BaseAppCompatActivity(), TimerListener {

    override fun onTimeUpdate(time: String) {
        notepadTimer.text = time
    }

    companion object {
        const val CHARACTER_MODEL_LIST_EXTRA = "NotePadActivity=characterModelListExtra"
        const val GAME_TYPE_MODEL_EXTRA = "NotePadActivity=gameTypeModelExtra"
    }

    private val controller by lazy {
        NotePadController(context = this)
    }
    private val characterModelArray by lazy {
        intent?.extras?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA)
    }
    private val gameTypeModel by lazy { intent?.extras?.getParcelable<GameTypeModel>(GAME_TYPE_MODEL_EXTRA) }

    override fun onBackPressed() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.key_warning_label),
        text = resources.getString(R.string.key_discard_message), basicDialogButtonBlock = {
              super.onBackPressed()
            }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad)
        setupToolbar()
        configureRecyclerView()
        configureTimer()
    }

    private fun setupToolbar() {
        setSupportActionBar(bottomAppBar)
        bottomAppBar.setNavigationOnClickListener { onBackPressed() }
        cardViewerButton.setOnClickListener {
            CardViewerBottomSheetFragment.show(
                supportFragmentManager = supportFragmentManager,
                charactersModelList = ArrayList(characterModelArray?.drop(n = 1) ?: return@setOnClickListener)
            )
        }
    }

    private fun configureRecyclerView() {
        notepadRowRecyclerView.adapter = RowAdapter(rowDataModelList = controller.getCluedoList(characterModelList = characterModelArray ?: return))
        notepadRowRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1))
                    bottomAppBar.elevation = 4.0f
                else
                    bottomAppBar.elevation = 32f
            }
        })
    }

    private fun configureTimer() {
        TimerHelper(timerListener = this)
    }

}
