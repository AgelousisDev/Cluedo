package com.agelousis.cluedonotepad.roomCreationDialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.databinding.RoomCreationDialogFragmentLayoutBinding
import com.agelousis.cluedonotepad.extensions.generatedRandomString
import com.agelousis.cluedonotepad.extensions.setLoaderState
import com.agelousis.cluedonotepad.firebase.FirebaseInstanceHelper
import com.agelousis.cluedonotepad.firebase.database.RealTimeDatabaseHelper
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.roomCreationDialog.presenters.RoomCreationPresenter
import com.agelousis.cluedonotepad.roomCreationDialog.presenters.RoomDialogDismissBlock
import com.agelousis.cluedonotepad.splash.SplashActivity
import com.agelousis.cluedonotepad.splash.enumerations.GameType
import com.agelousis.cluedonotepad.splash.models.GameTypeModel
import kotlinx.android.synthetic.main.room_creation_dialog_fragment_layout.*

class RoomCreationDialogFragment(private val roomDialogDismissBlock: RoomDialogDismissBlock): DialogFragment(), RoomCreationPresenter {

    companion object {
        fun show(supportFragmentManager: FragmentManager, roomDialogDismissBlock: RoomDialogDismissBlock) {
            with(RoomCreationDialogFragment(
                roomDialogDismissBlock = roomDialogDismissBlock
            )) {
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.ROOM_DIALOG_TAG)
        }
    }

    override fun onRoomGeneration() = roomDialogField.setText(9.generatedRandomString)

    override fun onOffline() {
        dismiss()
        roomDialogDismissBlock(
            GameTypeModel(
                gameType = GameType.OFFLINE
            )
        )
    }

    override fun onRoomJoined() =
        checkRoomAvailability(gameType = GameType.JOINED_ROOM)

    override fun onRoomCreation() =
        checkRoomAvailability(gameType = GameType.ROOM_CREATION)

    private var roomButtonsState: Boolean = false
        set(value) {
            field = value
            roomDialogJoinButton.alpha = if (value) 1.0f else 0.5f
            roomDialogJoinButton.isEnabled = value
            roomDialogCreationButton.alpha = if (value) 1.0f else 0.5f
            roomDialogCreationButton.isEnabled = value
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.attributes?.windowAnimations = R.style.DialogAnimation
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        RoomCreationDialogFragmentLayoutBinding.inflate(
            inflater, container, false
        ).also {
            it.currentChannel = MainApplication.currentChannel
            it.presenter = this
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        roomGenerationButton.isEnabled = MainApplication.connectionIsEstablished
        if (!MainApplication.connectionIsEstablished) {
            roomDialogField.isEnabled = false
            roomDialogFieldLayout.error = resources.getString(R.string.key_no_internet_connection_label)
        }
        roomDialogField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.contains(" ") == true) {
                    roomDialogField.setText(p0.trim())
                    roomDialogField.setSelection(p0.length - 1)
                }
                roomButtonsState = (p0?.length ?: 0) >= 9 && MainApplication.connectionIsEstablished
            }
        })
    }

    private fun pushUser(gameType: GameType) {
        FirebaseInstanceHelper.shared.initializeFirebaseToken {
            MainApplication.firebaseToken = it
            RealTimeDatabaseHelper.shared.addUser(
                user = User(
                    channel = roomDialogField.text?.toString(),
                    device = it,
                    character = (activity as? SplashActivity)?.characterViewModel?.characterArray?.firstOrNull()?.characterEnum
                )
            )
            dismiss()
            MainApplication.currentChannel = roomDialogField.text?.toString()
            roomDialogDismissBlock(
                GameTypeModel(
                    gameType = gameType,
                    channel = roomDialogField.text?.toString()
                )
            )
        }
    }

    private fun checkRoomAvailability(gameType: GameType) {
        (activity as? SplashActivity)?.setLoaderState(
            state = true
        )
        RealTimeDatabaseHelper.shared.searchChannel(
            channel = roomDialogField.text?.toString() ?: return
        ) { isAvailable ->
            (activity as? SplashActivity)?.setLoaderState(
                state = false
            )
            when(gameType) {
                GameType.ROOM_CREATION ->
                    if (isAvailable)
                        Toast.makeText(context, resources.getString(R.string.key_room_exists_message), Toast.LENGTH_SHORT).show()
                    else
                        pushUser(
                            gameType = GameType.ROOM_CREATION
                        )
                GameType.JOINED_ROOM ->
                    if (isAvailable)
                        pushUser(
                            gameType = gameType
                        )
                    else
                        Toast.makeText(context, resources.getString(R.string.key_room_not_exists_message), Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

}