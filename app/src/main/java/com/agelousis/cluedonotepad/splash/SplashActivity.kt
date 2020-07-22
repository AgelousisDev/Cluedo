package com.agelousis.cluedonotepad.splash

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.dialog.presenters.LanguagePresenter
import com.agelousis.cluedonotepad.extensions.*
import com.agelousis.cluedonotepad.firebase.database.RealTimeDatabaseHelper
import com.agelousis.cluedonotepad.firebase.models.User
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.roomCreationDialog.RoomCreationDialogFragment
import com.agelousis.cluedonotepad.splash.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.viewModels.CharacterViewModel
import com.agelousis.cluedonotepad.stats.StatsSheetFragment
import com.agelousis.cluedonotepad.stats.models.StatsModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseAppCompatActivity(), LanguagePresenter {

    companion object {
        const val RATE_REQUEST_CODE = 1
        const val LANGUAGE_DIALOG_STATE_EXTRA = "SplashActivity=languageDialogStateExtra"
    }

    private val sharedPreferences by lazy {
        getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE)
    }

    var characterViewModel: CharacterViewModel? = null
        set(value) {
            field = value
            value?.addDefaultRow(CharacterModel(characterNameHint = resources.getString(R.string.key_your_name_hint)))
        }

    private var lastSeekBarProgress = 0

    var statsModelList = arrayListOf<StatsModel>()

    override fun onLanguageSelected(languageCode: String) = refreshActivity(
        extras = Bundle().also {
            it.putBoolean(LANGUAGE_DIALOG_STATE_EXTRA, false)
        }
    )

    override fun onBackPressed() {
        when(sharedPreferences.ratingValue) {
            true -> super.onBackPressed()
            false ->
                showRateDialog(requestCode = RATE_REQUEST_CODE) {
                    sharedPreferences.setRatingValue(value = false)
                    super.onBackPressed()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupNightModeIdSaved()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseApp.initializeApp(this)
        configureViewModel()
        setupUI()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        showLanguageDialog()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refreshActivity()
    }

    private fun showLanguageDialog() {
        intent.extras.whenNull {
            BasicDialog.show(
                supportFragmentManager = supportFragmentManager,
                dialogType = BasicDialogType(
                    BasicDialogTypeEnum.LANGUAGE_SELECT,
                    title = resources.getString(R.string.key_language_label),
                    languagePresenter = this
                )
            )
        }
    }

    private fun setupNightModeIdSaved() {
        when (sharedPreferences.isNightMode) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupUI() {
        cluedoImageView.applyLightScaleAnimation()
        darkModeSwitch.isChecked = sharedPreferences?.isNightMode == 1 || isNightMode == 1
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences?.edit()) {
                this?.putInt(Constants.DARK_MODE_VALUE, if (isChecked) 1 else 0)
                this?.apply()
            }
            setupNightModeIdSaved()
            /*refreshActivity(
                extras = Bundle().also {
                    it.putBoolean(LANGUAGE_DIALOG_STATE_EXTRA, false)
                }
            )*/
        }

        playersSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                lastSeekBarProgress = seekBar?.progress ?: 0
                playButton.isEnabled = seekBar?.progress ?: 0 > 0
                statsButton.isEnabled = seekBar?.progress ?: 0 > 0
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cluedoImageView.visibility = if (progress > 0) View.GONE else View.VISIBLE
                darkModeSwitch.visibility = if (progress > 0) View.GONE else View.VISIBLE
                if (progress > lastSeekBarProgress)
                    (progress - lastSeekBarProgress).run {
                        characterViewModel?.addCharacter(characterModel = CharacterModel(characterNameHint = resources.getString(R.string.key_player_name_hint)))
                    }
                else if (progress < lastSeekBarProgress)
                    (lastSeekBarProgress - progress).run {
                        characterViewModel?.removeCharacter()
                    }

                (playersRecyclerView.adapter as? PlayersAdapter)?.update(appendState = progress > lastSeekBarProgress)
                lastSeekBarProgress = progress
            }
        })

        setupRecyclerView()
        playButton.setOnClickListener {
            if (isPlayersAvailable())
                RoomCreationDialogFragment.show(
                    supportFragmentManager = supportFragmentManager
                ) { gameTypeModel ->
                    startActivity(with(Intent(this, NotePadActivity::class.java)) {
                        putParcelableArrayListExtra(NotePadActivity.CHARACTER_MODEL_LIST_EXTRA, characterViewModel?.characterArray)
                        putExtra(NotePadActivity.GAME_TYPE_MODEL_EXTRA, gameTypeModel)
                        this
                    })
                }
        }
        statsButton.setOnClickListener {
            if (isPlayersAvailable()) {
                statsModelList.takeIf { it.isNotEmpty() && it.size != characterViewModel?.characterArray?.size ?: 0 }?.also {
                    statsModelList = ArrayList(statsModelList.subList(fromIndex = 0, toIndex = (characterViewModel?.characterArray?.size ?: 1) - 1))
                }
                characterViewModel?.characterArray?.map { characterModel -> characterModel.statsModel }?.forEachIndexed { index, statsModel ->
                    statsModelList.getOrNull(index = index)?.let {
                        it.playerName = statsModel.playerName
                        it.playerColor = statsModel.playerColor
                    } ?: statsModelList.add(with(statsModel) {
                        playerScore = statsModelList.getOrNull(index = index)?.playerScore ?: 0
                        this
                    })
                }
                StatsSheetFragment.show(
                    supportFragmentManager = supportFragmentManager,
                    statsModelList = ArrayList(statsModelList)
                )
            }
        }
    }

    private fun setupRecyclerView() {
        if (isPortrait) {
            val flexLayoutManager = FlexboxLayoutManager(this@SplashActivity, FlexDirection.ROW)
            flexLayoutManager.flexDirection = FlexDirection.ROW
            flexLayoutManager.justifyContent = JustifyContent.CENTER
            flexLayoutManager.alignItems = AlignItems.CENTER
            playersRecyclerView.layoutManager = flexLayoutManager
        }
        characterViewModel?.characterArray?.firstOrNull()?.let {
            characterViewModel?.characterArray?.clear()
            characterViewModel?.characterArray?.add(it)
        }
        val playersAdapter = PlayersAdapter(context = this, characterListModel = characterViewModel?.characterArray ?: listOf())
        playersRecyclerView.adapter = playersAdapter
    }

    private fun configureViewModel() {
        characterViewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)
    }

    private fun isPlayersAvailable(): Boolean {
        return if ((characterViewModel?.characterArray?.size ?: 0) < 2 || characterViewModel?.characterArray?.any { it.character == null || it.characterName.isNullOrEmpty() } == true ||
            characterViewModel?.characterArray?.mapNotNull { it.character }?.distinct()?.size ?: 0 < characterViewModel?.characterArray?.size ?: -1) {
            BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(
                title = resources.getString(R.string.key_warning_label),
                text = resources.getString(R.string.key_not_selected_players_message)
            )
            )
            false
        }
        else true
    }

    private fun refreshActivity(extras: Bundle? = null) {
        startActivity(Intent(this@SplashActivity, SplashActivity::class.java).also { intent ->
            extras?.let { bundle ->
                intent.putExtras(bundle)
            }
        })
        this@SplashActivity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            RATE_REQUEST_CODE -> sharedPreferences.setRatingValue(value = true)
        }
    }

}
