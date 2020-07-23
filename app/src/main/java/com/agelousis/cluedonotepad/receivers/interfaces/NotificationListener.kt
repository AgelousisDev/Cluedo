package com.agelousis.cluedonotepad.receivers.interfaces

import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel

interface NotificationListener {
    fun onNotificationReceived(firebaseMessageDataModel: FirebaseMessageDataModel)
}