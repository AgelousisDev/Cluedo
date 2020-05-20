package com.agelousis.cluedonotepad.utils.helpers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

object LocaleHelper {

    @Suppress("DEPRECATION")
    fun updateLocale(context: Context, language: String): Context? {
        var newContext = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = newContext.resources
        val config = Configuration(res.configuration)
        newContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
            newContext.createConfigurationContext(config)
        }
        else {
            config.setLocale(locale)
            newContext.resources.updateConfiguration(config, newContext.resources.displayMetrics)
            newContext
        }
        return newContext
    }

}