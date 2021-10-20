package com.agelousis.cluedonotepad.utils.helpers

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

typealias ConnectionBlock = (status: Boolean) -> Unit

object ConnectionHelper {

    fun icConnectionAvailable(connectionBlock: ConnectionBlock) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val httpURLConnection =
                    URL("https://google.com").openConnection() as? HttpURLConnection ?: return@runCatching
                httpURLConnection.setRequestProperty("User-Agent", "Test")
                httpURLConnection.setRequestProperty("Connection", "close")
                httpURLConnection.doInput = true
                httpURLConnection.connectTimeout = 10000
                try {
                    httpURLConnection.connect()
                    val responseCode = httpURLConnection.responseCode
                    connectionBlock(responseCode == 200)
                } catch (e: Exception) {
                    connectionBlock(false)
                }
            }
        }
    }

}