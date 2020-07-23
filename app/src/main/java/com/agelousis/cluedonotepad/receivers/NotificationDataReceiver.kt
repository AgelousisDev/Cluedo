package com.agelousis.cluedonotepad.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.receivers.interfaces.NotificationListener

class NotificationDataReceiver: BroadcastReceiver() {

    private var notificationListener: NotificationListener? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        notificationListener?.onNotificationReceived(
            firebaseMessageDataModel = p1?.extras?.getParcelable(NotePadActivity.NOTIFICATION_DATA_MODEL_EXTRA) ?: return
        )
    }

}