package com.agelousis.cluedonotepad.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.extensions.savedLanguage
import com.agelousis.cluedonotepad.utils.helpers.LocaleHelper

@SuppressLint("Registered")
open class BaseAppCompatActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        var context = newBase
        newBase?.getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE)?.savedLanguage?.let {
            context = LocaleHelper.updateLocale(
                context = newBase,
                language = it
            )
        }
        super.attachBaseContext(context)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

}