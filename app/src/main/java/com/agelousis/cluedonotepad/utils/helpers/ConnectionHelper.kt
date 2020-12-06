package com.agelousis.cluedonotepad.utils.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

typealias ConnectionBlock = (status: Boolean) -> Unit
object ConnectionHelper {

    fun icConnectionAvailable(connectionBlock: ConnectionBlock) {
        val httpURLConnection = URL("https://google.com").openConnection() as? HttpURLConnection ?: return
        httpURLConnection.setRequestProperty("User-Agent", "Test")
        httpURLConnection.setRequestProperty("Connection", "close")
        httpURLConnection.doInput = true
        httpURLConnection.connectTimeout = 10000
        GlobalScope.launch(Dispatchers.IO) {
            try {
                httpURLConnection.connect()
                val responseCode = httpURLConnection.responseCode
                withContext(Dispatchers.Main) {
                    connectionBlock(responseCode == 200)
                }
            }
            catch (e: Exception) {
                connectionBlock(false)
            }
        }
    }

}