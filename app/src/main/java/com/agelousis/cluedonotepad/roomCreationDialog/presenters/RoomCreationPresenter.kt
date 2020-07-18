package com.agelousis.cluedonotepad.roomCreationDialog.presenters

typealias RoomDialogDismissBlock = () -> Unit
interface RoomCreationPresenter {
    fun onOffline()
    fun onRoomCreation()
    fun onRoomJoined()
}