package com.agelousis.cluedonotepad.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.constants.IndexedLoopBlock
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView

fun Context.showCharacterOptions(title: String, adapterPosition: Int, characterPresenter: CharacterPresenter) {
    BasicDialog.show(supportFragmentManager = (this as AppCompatActivity).supportFragmentManager, dialogType =
        BasicDialogType(basicDialogTypeEnum = with(BasicDialogTypeEnum.CHARACTER_SELECT) {
            characterPosition = adapterPosition
            this
        }, title = title, characterPresenter = characterPresenter))
}

fun Int.run(indexedLoopBlock: IndexedLoopBlock) {
    for (i in 0 until this)
        indexedLoopBlock(i)
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val SharedPreferences?.isNightMode: Int
    get() = this?.getInt(Constants.DARK_MODE_VALUE, -1) ?: -1

val Context?.isNightMode: Int
    get() = when(this?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_NO -> 0
        Configuration.UI_MODE_NIGHT_YES -> 1
        else -> -1
    }

val Context.isPortrait: Boolean
    get() = resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

fun View.applyLightScaleAnimation(duration: Long? = null) {
    val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0.98f)
    scaleX.repeatMode = ObjectAnimator.REVERSE
    scaleX.repeatCount = ObjectAnimator.INFINITE
    scaleX.interpolator = LinearInterpolator()
    scaleX.duration = duration ?: 1000

    val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0.98f)
    scaleY.repeatMode = ObjectAnimator.REVERSE
    scaleY.repeatCount = ObjectAnimator.INFINITE
    scaleY.interpolator = LinearInterpolator()
    scaleY.duration = duration ?: 1000

    with(AnimatorSet()) {
        playTogether(scaleX, scaleY)
        this
    }.start()
}

typealias DismissCallback = () -> Unit
fun Context.showRateDialog(requestCode: Int, dismissCallback: DismissCallback) {
    val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog)
        .setTitle(resources.getString(R.string.key_rate_label))
        .setMessage(resources.getString(R.string.key_rate_message))
        .setNegativeButton(resources.getString(R.string.key_exit_label)) { dialog, _ ->
            dialog.dismiss()
            dismissCallback()
        }
        .setPositiveButton(resources.getString(R.string.key_rate_label)) { dialog, _ ->
            dialog.dismiss()
            (this as? AppCompatActivity)?.startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.playStoreUrl)), requestCode)
        }
    val dialog = builder.create()
    dialog.show()
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
}

fun SharedPreferences.setRatingValue(value: Boolean) {
    edit().also {
        it.putBoolean(Constants.RATE_VALUE, value)
        it.apply()
    }
}

val SharedPreferences.ratingValue: Boolean
    get() = getBoolean(Constants.RATE_VALUE, false)

var SharedPreferences.savedLanguage
    set(value) {
        edit().also {
            it.putString(Constants.LANGUAGE_PREFS_KEY, value)
            it.apply()
        }
    }
    get() = getString(Constants.LANGUAGE_PREFS_KEY, null)

fun <T> T?.whenNull(receiver: () -> Unit): T? {
    return if (this == null) {
        receiver.invoke()
        null
    } else this
}

inline fun <T> Iterable<T>.forEachIfEach(predicate: (T) -> Boolean, action: (T) -> Unit) {
    for (element in this)
        if (predicate(element))
            action(element)
}


@BindingAdapter("srcCompat")
fun setSrcCompat(appCompatImageView: AppCompatImageView, drawableId: Int?) {
    drawableId?.let {
        appCompatImageView.setImageResource(drawableId)
    }
}

@BindingAdapter("textBold")
fun setBold(appCompatTextView: MaterialTextView, state: Boolean) {
    if (state)
        appCompatTextView.typeface = Typeface.create(appCompatTextView.typeface, Typeface.BOLD)
}

@BindingAdapter("customBackground")
fun setCustomBackground(viewGroup: ViewGroup, drawableId: Int?) {
    drawableId?.let { viewGroup.setBackgroundResource(it) }
}