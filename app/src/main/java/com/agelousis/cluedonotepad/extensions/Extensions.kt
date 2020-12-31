package com.agelousis.cluedonotepad.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.enumerations.ItemHeaderType
import com.agelousis.cluedonotepad.cardViewer.models.ItemModel
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.constants.IndexedLoopBlock
import com.agelousis.cluedonotepad.custom.loader_dialog.LoaderDialog
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.splash.enumerations.Language
import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

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

/*val SharedPreferences?.isNightMode: Int
    get() = this?.getInt(Constants.DARK_MODE_VALUE, -1) ?: -1

val Context?.isNightMode: Int
    get() = when(this?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_NO -> 0
        Configuration.UI_MODE_NIGHT_YES -> 1
        else -> -1
    }

val Context.isPortrait: Boolean
    get() = resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

 */

val Context.isLandscape: Boolean
    get() = resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE

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

inline fun <T, J> Iterable<T>.firstOrNullWithType(typeBlock: (T) -> J?, predicate: (J?) -> Boolean): J? {
    for (element in this) {
        val block = typeBlock(element)
        if (predicate(block))
            return typeBlock(element)
    }
    return null
}
fun AppCompatActivity.setLoaderState(state: Boolean) =
    if (state)
        LoaderDialog.show(
            supportFragmentManager = supportFragmentManager
        )
    else
        LoaderDialog.hide(
            supportFragmentManager = supportFragmentManager
        )

val Int.generatedRandomString: String
    get() {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqstuvwxyz0123456789?!@#$%&*"
        val salt = StringBuilder()
        val rnd = Random()
        while(salt.length < this) {
            val index: Int = (rnd.nextFloat() * chars.length).toInt()
            salt.append(chars[index])
        }
        return salt.toString()
    }

fun Context.makeSoundNotification() {
    try {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(this, notification)
        ringtone.play()
    } catch (e: Exception) {}
}

val Resources.currentLanguage: Language?
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Language.values().firstOrNull { it.locale == configuration.locales[0].language.toLowerCase(Locale.getDefault()) }
        else
            Language.values().firstOrNull { it.locale == configuration.locale.language.toLowerCase(Locale.getDefault()) }

fun AppCompatImageView.setImageUri(uri: Uri) {
    val url = URL(uri.toString())
    val httpUrlConnection = url.openConnection() as? HttpURLConnection
    httpUrlConnection?.doInput = true
    CoroutineScope(Dispatchers.Default).launch {
        val imageInputStream = try { httpUrlConnection?.inputStream ?: return@launch }  catch (e: Exception) { return@launch }
        val bitmap = BitmapFactory.decodeStream(imageInputStream)
        withContext(Dispatchers.Main) {
            setImageDrawable(
                RoundedBitmapDrawableFactory.create(
                    context.resources,
                    bitmap
                ).also { roundedBitmapDrawable ->
                    roundedBitmapDrawable.isCircular = true
                }
            )
        }
    }
}

fun AppCompatImageView.setAnimatedImageResourceId(resourceId: Int?) {
    resourceId?.let {
        post {
            alpha = 0.0f
            setImageResource(it)
            animate().alpha(1.0f).interpolator = LinearInterpolator()
        }
    }
}

/*fun Window.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDecorFitsSystemWindows(false)
        insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    else
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
}
*/

/*val Window.hasNotch
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                decorView.rootWindowInsets?.displayCutout != null
            else false
*/

fun after(millis: Long, runnable: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        runnable,
        millis
    )
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

@BindingAdapter("itemModel")
fun setItemModel(materialTextView: MaterialTextView, itemModel: ItemModel?) {
    itemModel?.let {
        materialTextView.text = when(it.itemHeaderType) {
            ItemHeaderType.WHO -> materialTextView.context.resources.getStringArray(R.array.key_characters_array).getOrNull(index = it.itemPosition)
            ItemHeaderType.WHAT -> materialTextView.context.resources.getStringArray(R.array.key_tools_array).getOrNull(index = it.itemPosition)
            ItemHeaderType.WHERE -> materialTextView.context.resources.getStringArray(R.array.key_rooms_array).getOrNull(index = it.itemPosition)
        }
    }
}

@BindingAdapter("itemModel")
fun setItemModel(appCompatImageView: AppCompatImageView, itemModel: ItemModel?) {
    itemModel?.let {
        val arrayOfIcons = when(it.itemHeaderType) {
            ItemHeaderType.WHO -> appCompatImageView.context.resources.obtainTypedArray(R.array.key_character_icons_array)
            ItemHeaderType.WHAT -> appCompatImageView.context.resources.obtainTypedArray(R.array.key_tool_icons_array)
            ItemHeaderType.WHERE -> appCompatImageView.context.resources.obtainTypedArray(R.array.key_room_icons_array)
        }
        appCompatImageView.setImageResource(arrayOfIcons.getResourceId(it.itemPosition, -1))
        arrayOfIcons.recycle()
    }
}

@BindingAdapter("picassoImageResource")
fun setPicassoImageResource(appCompatImageView: AppCompatImageView, resourceId: Int?) {
    appCompatImageView.post {
        resourceId?.let {
            Picasso.get().load(it).resize(appCompatImageView.width * 2, 0).into(appCompatImageView)
        }
    }
}