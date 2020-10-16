package com.agelousis.cluedonotepad.application

import android.app.Application

class MainApplication: Application() {

    companion object {
        var connectionIsEstablished = false
        var firebaseToken: String? = null
        var currentChannel: String? = null
    }

}