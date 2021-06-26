package com.agelousis.cluedonotepad.main

import android.app.Activity
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.cards.EmptyModel
import com.agelousis.cluedonotepad.cards.interfaces.CardsUpdateListener
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.databinding.ActivityNotepadBinding
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.extensions.hideSystemUI
import com.agelousis.cluedonotepad.extensions.isLandscape
import com.agelousis.cluedonotepad.extensions.playSoundWithName
import com.agelousis.cluedonotepad.extensions.setLoaderState
import com.agelousis.cluedonotepad.firebase.database.RealTimeDatabaseHelper
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.main.adapters.SuspectFragmentAdapter
import com.agelousis.cluedonotepad.main.viewModel.NotePadViewModel
import com.agelousis.cluedonotepad.notificationDataViewer.NotificationDataViewerDialogFragment
import com.agelousis.cluedonotepad.receivers.NotificationDataReceiver
import com.agelousis.cluedonotepad.receivers.interfaces.NotificationListener
import com.agelousis.cluedonotepad.splash.enumerations.GameType
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.models.GameTypeModel
import com.agelousis.cluedonotepad.utils.components.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

class NotePadActivity : BaseAppCompatActivity(), NotificationListener {

    companion object {
        const val CHARACTER_MODEL_LIST_EXTRA = "NotePadActivity=characterModelListExtra"
        const val GAME_TYPE_MODEL_EXTRA = "NotePadActivity=gameTypeModelExtra"
        const val NOTIFICATION_DATA_MODEL_EXTRA = "NotePadActivity=notificationDataModelExtra"
    }

    override fun onNotificationReceived(firebaseMessageDataModel: FirebaseMessageDataModel) {
        if (firebaseMessageDataModel !in cardsSharedList) {
            if (cardsSharedList.any { it is EmptyModel })
                cardsSharedList.clear()
            cardsSharedList.add(firebaseMessageDataModel)
            cardsUpdateListener?.onUpdate()
        }
        this playSoundWithName R.raw.mystery_sound_effect
        NotificationDataViewerDialogFragment.show(
            supportFragmentManager = supportFragmentManager,
            firebaseMessageDataModel = firebaseMessageDataModel
        )
    }

    private lateinit var binding: ActivityNotepadBinding
    val viewModel: NotePadViewModel by viewModels()
    val characterModelArray by lazy {
        intent?.extras?.getParcelableArrayList<CharacterModel>(CHARACTER_MODEL_LIST_EXTRA)
    }
    val gameTypeModel by lazy { intent?.extras?.getParcelable<GameTypeModel>(GAME_TYPE_MODEL_EXTRA) }
    private val users by lazy { arrayListOf<User>() }
    private val notificationDataReceiver by lazy { NotificationDataReceiver().also {
        it.notificationListener = this
    } }
    private val notificationIntentFilter by lazy { IntentFilter(Constants.SHOW_NOTIFICATION_INTENT_ACTION) }
    val cardsSharedList by lazy {
        arrayListOf<Any>(
            EmptyModel(
                imageIconResource = R.drawable.ic_image,
                title = resources.getString(R.string.key_cards_label),
                message = resources.getString(R.string.key_no_cards_message)
            )
        )
    }
    var cardsUpdateListener: CardsUpdateListener? = null
    private val suspectFragmentAdapter by lazy {
        SuspectFragmentAdapter(
            fragmentActivity = this,
            hasSharingAccess = gameTypeModel?.gameType == GameType.ROOM_CREATION
                    || gameTypeModel?.gameType == GameType.JOINED_ROOM
        )
    }

    override fun onBackPressed() {
        BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(title = resources.getString(R.string.key_warning_label),
        text = resources.getString(R.string.key_discard_message), basicDialogButtonBlock = {
              removeChannel {
                  gameTypeModel?.gameType == GameType.ROOM_CREATION ||
                          gameTypeModel?.gameType == GameType.JOINED_ROOM
              }
                setResult(Activity.RESULT_OK)
                super.onBackPressed()
            }))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isLandscape)
            window?.hideSystemUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        binding = ActivityNotepadBinding.inflate(layoutInflater)
        binding.cluedoBannerResource = R.drawable.cluedo_banner
        setContentView(binding.root)
        clearSelectedCardsCache()
        configureViewPagerAndTabLayout()
    }

    private fun clearSelectedCardsCache() {
        MainApplication.currentSelectedCards.clear()
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

    private fun configureViewPagerAndTabLayout() {
        findViewById<ViewPager2>(R.id.notePadViewPager).apply {
            setPageTransformer(
                ZoomOutPageTransformer()
            )
            adapter = suspectFragmentAdapter
            offscreenPageLimit = if (gameTypeModel?.gameType == GameType.ROOM_CREATION || gameTypeModel?.gameType == GameType.JOINED_ROOM) 5 else 3
            TabLayoutMediator(
                binding.notePadTabLayout,
                this
            ) { tab, position ->
                tab.text = suspectFragmentAdapter.getPageTitle(
                    position = position
                )
                tab.setIcon(
                    suspectFragmentAdapter.getPageIcon(
                        position = position
                    ) ?: return@TabLayoutMediator
                )
            }.attach()
        }
    }

    private fun removeChannel(predicate: () -> Boolean) {
        if (predicate())
            RealTimeDatabaseHelper.shared.deleteChannel(
                channel = gameTypeModel?.channel ?: return
            )
    }

    fun initializeUsers(character: Character?, block: (users: List<User?>) -> Unit) {
        if (users.isNotEmpty())
            block(
                character?.let {
                    listOf(
                        users.firstOrNull { user -> user.character == it }
                    )
                } ?: users
            )
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
                users.addAll(
                    it
                )
                block(
                    character?.let {
                        listOf(
                            users.firstOrNull { user -> user.character == it }
                        )
                    } ?: users
                )
            }
        }
    }

}
