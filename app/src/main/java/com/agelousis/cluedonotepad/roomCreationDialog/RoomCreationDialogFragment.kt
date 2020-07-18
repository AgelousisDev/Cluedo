package com.agelousis.cluedonotepad.roomCreationDialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.databinding.RoomCreationDialogFragmentLayoutBinding
import com.agelousis.cluedonotepad.roomCreationDialog.presenters.RoomCreationPresenter
import com.agelousis.cluedonotepad.roomCreationDialog.presenters.RoomDialogDismissBlock
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

    override fun onOffline() {
        dismiss()
        roomDialogDismissBlock()
    }

    override fun onRoomJoined() {
        dismiss()
        roomDialogDismissBlock()
    }

    override fun onRoomCreation() {
        dismiss()
        roomDialogDismissBlock()
    }

    private var roomButtonsState: Boolean = false
        set(value) {
            field = value
            roomDialogJoinButton.alpha = if (value) 1.0f else 0.5f
            roomDialogJoinButton.isEnabled = value
            roomDialogCreationButton.alpha = if (value) 1.0f else 0.5f
            roomDialogCreationButton.isEnabled = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return RoomCreationDialogFragmentLayoutBinding.inflate(
            inflater, container, false
        ).also {
            it.presenter = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        roomDialogField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.contains(" ") == true) {
                    roomDialogField.setText(p0.trim())
                    roomDialogField.setSelection(p0.length - 1)
                }
                roomButtonsState = (p0?.length ?: 0) >= 6
            }
        })
    }

}