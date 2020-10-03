package com.agelousis.cluedonotepad.main

import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.cardViewer.CardViewerBottomSheetFragment
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.extensions.makeSoundNotification
import com.agelousis.cluedonotepad.extensions.setLoaderState
import com.agelousis.cluedonotepad.firebase.database.RealTimeDatabaseHelper
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.main.adapters.SuspectFragmentAdapter
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

    override fun onNotificationReceived(firebaseMessageDataModel: FirebaseMessageDataModel) {
        makeSoundNotification()
        NotificationDataViewerDialogFragment.show(
            supportFragmentManager = supportFragmentManager,
            firebaseMessageDataModel = firebaseMessageDataModel
        )
    }

    val viewModel by lazy { ViewModelProvider(this).get(NotePadViewModel::class.java) }
    val characterModelArray by lazy {
        intent?.extras?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA)
    }
    private val gameTypeModel by lazy { intent?.extras?.getParcelable<GameTypeModel>(GAME_TYPE_MODEL_EXTRA) }
    private val users by lazy { arrayListOf<User>() }
    private val notificationDataReceiver by lazy { NotificationDataReceiver().also {
        it.notificationListener = this
    } }
    private val notificationIntentFilter by lazy { IntentFilter(Constants.SHOW_NOTIFICATION_INTENT_ACTION) }

    override fun onBackPressed() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.key_warning_label),
        text = resources.getString(R.string.key_discard_message), basicDialogButtonBlock = {
              removeChannel {
                  gameTypeModel?.gameType == GameType.ROOM_CREATION ||
                          gameTypeModel?.gameType == GameType.JOINED_ROOM
              }
              super.onBackPressed()
            }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad)
        setupToolbar()
        configureViewPagerAndTabLayout()
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
        bottomAppBarTitle.text = gameTypeModel?.channel ?: resources.getString(R.string.app_name)
        cardViewerButton.setOnClickListener {
            CardViewerBottomSheetFragment.show(
                supportFragmentManager = supportFragmentManager,
                charactersModelList = ArrayList(characterModelArray?.drop(n = 1) ?: return@setOnClickListener)
            )
        }
    }

    private fun configureViewPagerAndTabLayout() {
        notePadViewPager.adapter = SuspectFragmentAdapter(
            context = this,
            supportFragmentManager = supportFragmentManager
        )
        notePadViewPager.offscreenPageLimit = 3
        notePadTabLayout.setupWithViewPager(notePadViewPager)
    }

    private fun configureTimer() {
        TimerHelper(timerListener = this)
    }

    private fun initializeGameRoom(predicate: () -> Boolean) {
        if (predicate())
            cardViewerButton.show()
        else
            cardViewerButton.hide()
    }

    private fun removeChannel(predicate: () -> Boolean) {
        if (predicate())
            RealTimeDatabaseHelper.shared.deleteChannel(
                channel = gameTypeModel?.channel ?: return
            )
    }

    fun initializeUser(character: Character, block: (user: User) -> Unit) {
        if (users.isNotEmpty())
            block(users.firstOrNull { user -> user.character == character } ?: return)
        else {
            setLoaderState(
                state = true
            )
            RealTimeDatabaseHelper.shared.getUsers(
                channel = gameTypeModel?.channel ?: return
            ) inner@ {
                setLoaderState(
                    state = false
                )
                users.addAll(it)
                block(users.firstOrNull { user -> user.character == character } ?: return@inner)
            }
        }
    }

}
