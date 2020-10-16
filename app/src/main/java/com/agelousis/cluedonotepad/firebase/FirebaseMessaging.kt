package com.agelousis.cluedonotepad.firebase

import android.content.Intent
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

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
                    itemModel = Gson().fromJson(p0.data["itemModel"], ItemModel::class.java)
                )
                )
            intent.action = Constants.SHOW_NOTIFICATION_INTENT_ACTION
            sendBroadcast(intent)
        }
    }

}