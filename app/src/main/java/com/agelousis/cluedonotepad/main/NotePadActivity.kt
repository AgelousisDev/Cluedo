package com.agelousis.cluedonotepad.main

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.main.adapters.RowAdapter
import com.agelousis.cluedonotepad.main.controller.NotePadController
import com.agelousis.cluedonotepad.main.timer.TimerHelper
import com.agelousis.cluedonotepad.main.timer.TimerListener
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import kotlinx.android.synthetic.main.activity_notepad.*

class NotePadActivity : AppCompatActivity(), TimerListener {

    override fun onTimeUpdate(time: String) {
        notepadTimer.text = time
    }

    override fun onFiveMinutes() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.inflicted)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    companion object {
        const val CHARACTER_MODEL_LIST_EXTRA = "NotePadActivity=characterModelListExtra"
    }

    private val controller by lazy {
        NotePadController(context = this)
    }

    private val characterModelArray by lazy {
        intent?.extras?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA)
    }

    override fun onBackPressed() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.key_warning_label),
        text = resources.getString(R.string.key_discard_message), basicDialogButtonBlock = {
              super.onBackPressed()
            }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad)
        configureRecyclerView()
        configureTimer()
    }

    private fun configureRecyclerView() {
        notepadRowRecyclerView.adapter = RowAdapter(rowDataModelList = controller.getCluedoList(characterModelList = characterModelArray ?: return))
    }

    private fun configureTimer() {
        TimerHelper(timerListener = this)
    }

}
