package com.agelousis.cluedonotepad.roomCreationDialog.presenters

import com.agelousis.cluedonotepad.splash.models.GameTypeModel

typealias RoomDialogDismissBlock = (gameTypeModel: GameTypeModel) -> Unit
interface RoomCreationPresenter {
    fun onOffline()
    fun onRoomCreation()
    fun onRoomJoined()
}