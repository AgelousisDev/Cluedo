package com.agelousis.cluedonotepad.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.constants.IndexedLoopBlock
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter

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

@BindingAdapter("srcCompat")
fun setSrcCompat(appCompatImageView: AppCompatImageView, drawableId: Int?) {
    drawableId?.let {
        appCompatImageView.setImageResource(drawableId)
    }
}

@BindingAdapter("textBold")
fun setBold(appCompatTextView: AppCompatTextView, state: Boolean) {
    if (state)
        appCompatTextView.typeface = Typeface.create(appCompatTextView.typeface, Typeface.BOLD)
}

@BindingAdapter("customBackground")
fun setCustomBackground(viewGroup: ViewGroup, drawableId: Int?) {
    drawableId?.let { viewGroup.setBackgroundResource(it) }
}
