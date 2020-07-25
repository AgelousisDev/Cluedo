package com.agelousis.cluedonotepad.utils.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

typealias ConnectionBlock = (status: Boolean) -> Unit
object ConnectionHelper {

    suspend fun icConnectionAvailable(connectionBlock: ConnectionBlock) {
        withContext(Dispatchers.IO) {
            try {
                (URL("https://google.com").openConnection() as HttpURLConnection).apply {
                    setRequestProperty("User-Agent", "Test")
                    setRequestProperty("Connection", "close")
                    doInput = true
                    connectTimeout = 10000
                    connect()
                    val responseCode = responseCode
                    withContext(Dispatchers.Main) {
                        connectionBlock(responseCode == 200)
                    }
                }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    connectionBlock(false)
                }
            }
        }
    }

}