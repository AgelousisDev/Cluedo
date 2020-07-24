package com.agelousis.cluedonotepad.main

import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.cardViewer.CardViewerBottomSheetFragment
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.extensions.setLoaderState
import com.agelousis.cluedonotepad.firebase.database.RealTimeDatabaseHelper
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.main.adapters.RowAdapter
import com.agelousis.cluedonotepad.main.controller.NotePadController
import com.agelousis.cluedonotepad.main.timer.TimerHelper
import com.agelousis.cluedonotepad.main.timer.TimerListener
import com.agelousis.cluedonotepad.main.viewModel.NotePadViewModel
import com.agelousis.cluedonotepad.notificationDataViewer.NotificationDataViewerDialogFragment
import com.agelousis.cluedonotepad.receivers.NotificationDataReceiver
import com.agelousis.cluedonotepad.receivers.interfaces.NotificationListener
import com.agelousis.cluedonotepad.splash.enumerations.GameType
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.models.GameTypeModel
import kotlinx.android.synthetic.main.activity_notepad.*

class NotePadActivity : BaseAppCompatActivity(), TimerListener, NotificationListener {

    companion object {
        const val CHARACTER_MODEL_LIST_EXTRA = "NotePadActivity=characterModelListExtra"
        const val GAME_TYPE_MODEL_EXTRA = "NotePadActivity=gameTypeModelExtra"
        const val NOTIFICATION_DATA_MODEL_EXTRA = "NotePadActivity=notificationDataModelExtra"
    }

    override fun onTimeUpdate(time: String) {
        notepadTimer.text = time
    }

    override fun onNotificationReceived(firebaseMessageDataModel: FirebaseMessageDataModel) =
        NotificationDataViewerDialogFragment.show(
            supportFragmentManager = supportFragmentManager,
            firebaseMessageDataModel = firebaseMessageDataModel
        )

    val viewModel by lazy { ViewModelProvider(this).get(NotePadViewModel::class.java) }
    private val controller by lazy {
        NotePadController(context = this)
    }
    private val characterModelArray by lazy {
        intent?.extras?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA)
    }
    private val gameTypeModel by lazy { intent?.extras?.getParcelable<GameTypeModel>(GAME_TYPE_MODEL_EXTRA) }
    val users by lazy { arrayListOf<User>() }
    private val notificationDataReceiver by lazy { NotificationDataReceiver().also {
        it.notificationListener = this
    } }
    private val notificationIntentFilter by lazy { IntentFilter(Constants.SHOW_NOTIFICATION_INTENT_ACTION) }

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
        initializeGameRoom {
            gameTypeModel?.gameType == GameType.ROOM_CREATION ||
                    gameTypeModel?.gameType == GameType.JOINED_ROOM
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            notificationDataReceiver,
            notificationIntentFilter
        )
    }

    override fun onPause() {
        unregisterReceiver(
            notificationDataReceiver
        )
        super.onPause()
    }

    private fun setupToolbar() {
        setSupportActionBar(bottomAppBar)
        bottomAppBarTitle.text = gameTypeModel?.channel ?: resources.getString(R.string.app_name)
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

    private fun initializeGameRoom(predicate: () -> Boolean) {
        if (predicate()) {
            setLoaderState(
                state = true
            )
            RealTimeDatabaseHelper.shared.getUsers(
                channel = gameTypeModel?.channel ?: return
            ) {
                setLoaderState(
                    state = false
                )
                users.addAll(it)
            }
            cardViewerButton.show()
        }
        else
            cardViewerButton.hide()
    }

}
