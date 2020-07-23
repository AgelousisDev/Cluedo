package com.agelousis.cluedonotepad.firebase

import android.content.Intent
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Intent().also { intent ->
            intent.putExtra(
                NotePadActivity.NOTIFICATION_DATA_MODEL_EXTRA,
                FirebaseMessageDataModel(
                    itemHeaderType = ItemHeaderType.valueOf(
                        value = p0.data["itemHeaderType"] ?: return@also
                    ),
                    itemModel = ItemModel(
                        item = p0.data["itemModel"] ?: return@also,
                        itemHeaderType = ItemHeaderType.valueOf(
                            value = p0.data["itemHeaderType"] ?: return@also
                        )
                    )
                )
                )
            intent.action = "SHOW_NOTIFICATION"
            sendBroadcast(intent)
        }
    }

}