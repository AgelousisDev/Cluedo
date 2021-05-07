package com.agelousis.cluedonotepad.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.base.BaseAppCompatActivity
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.databinding.ActivitySplashBinding
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.dialog.presenters.LanguagePresenter
import com.agelousis.cluedonotepad.extensions.*
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.roomCreationDialog.RoomCreationDialogFragment
import com.agelousis.cluedonotepad.splash.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.splash.enumerations.GameType
import com.agelousis.cluedonotepad.splash.enumerations.Language
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.models.GameTypeModel
import com.agelousis.cluedonotepad.splash.viewModels.CharacterViewModel
import com.agelousis.cluedonotepad.stats.StatsSheetFragment
import com.agelousis.cluedonotepad.stats.models.StatsModel
import com.agelousis.cluedonotepad.utils.helpers.ConnectionHelper
import com.agelousis.cluedonotepad.utils.helpers.ReviewManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : BaseAppCompatActivity(), LanguagePresenter {

    companion object {
        const val LANGUAGE_DIALOG_STATE_EXTRA = "SplashActivity=languageDialogStateExtra"
    }

    private lateinit var binding: ActivitySplashBinding
    private val sharedPreferences by lazy {
        getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE)
    }

    var characterViewModel: CharacterViewModel? = null
        set(value) {
            field = value
            value?.addDefaultRow(CharacterModel(characterNameHint = resources.getString(R.string.key_your_name_hint)))
        }

    private var lastSeekBarProgress = 0
    private var seekBarIsTracking = false

    var statsModelList = arrayListOf<StatsModel>()

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        checkGoogleSignIn(
            data = result.data ?: return@registerForActivityResult
        )
    }
    private val notePadLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            initializeReviewManager()
    }

    override fun onLanguageSelected(language: Language) = refreshActivity(
        extras = Bundle().also {
            it.putBoolean(LANGUAGE_DIALOG_STATE_EXTRA, false)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeGoogleServices()
        configureViewModel()
        setupUI()
        initializeConnectionState()
    }

    private fun initializeGoogleServices() {
        FirebaseApp.initializeApp(this)
        Firebase.auth.currentUser.whenNull {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(
                signInIntent
            )
        }?.let { googleUser ->
            binding.googleUserImage.visibility = View.VISIBLE
            binding.googleUserImage.setImageUri(
                uri = googleUser.photoUrl ?: return@let
            )
        }
    }

    private fun showLanguageDialogIf(predicate: () -> Boolean) {
        if (predicate())
            BasicDialog.show(
                supportFragmentManager = supportFragmentManager,
                dialogType = BasicDialogType(
                    BasicDialogTypeEnum.LANGUAGE_SELECT,
                    title = resources.getString(R.string.key_language_label),
                    languagePresenter = this
                )
            )
    }

    private fun setupUI() {
        binding.cluedoImageView.applyLightScaleAnimation()
        binding.playersSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBarIsTracking = true
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                lastSeekBarProgress = seekBar?.progress ?: 0
                binding.playButton.isEnabled = seekBar?.progress ?: 0 > 0
                binding.statsButton.isEnabled = seekBar?.progress ?: 0 > 0
                seekBarIsTracking = false
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!seekBarIsTracking)
                    return
                binding.cluedoImageView.visibility = if (progress > 0) View.GONE else View.VISIBLE
                if (progress > lastSeekBarProgress)
                    (progress - lastSeekBarProgress).run {
                        characterViewModel?.addCharacter(characterModel = CharacterModel(characterNameHint = resources.getString(R.string.key_player_name_hint)))
                    }
                else if (progress < lastSeekBarProgress)
                    (lastSeekBarProgress - progress).run {
                        characterViewModel?.removeCharacter()
                    }
                (binding.playersRecyclerView.adapter as? PlayersAdapter)?.reloadData()
                lastSeekBarProgress = progress
            }
        })

        setupRecyclerView()
        binding.playButton.setOnClickListener {
            if (isPlayersAvailable())
                Firebase.auth.currentUser.whenNull {
                    openNotePad(
                        gameTypeModel = GameTypeModel(
                            gameType = GameType.OFFLINE
                        )
                    )
                }?.let {
                    RoomCreationDialogFragment.show(
                        supportFragmentManager = supportFragmentManager
                    ) { gameTypeModel ->
                        openNotePad(
                            gameTypeModel = gameTypeModel
                        )
                    }
                }
        }
        binding.statsButton.setOnClickListener {
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
        sharedPreferences.savedLanguage.whenNull {
            binding.languageButton.setImageResource(resources.currentLanguage?.icon ?: R.drawable.ic_language)
        }?.let { savedLanguage ->
            binding.languageButton.setImageResource(Language.values().firstOrNull { it.locale == savedLanguage }?.icon ?: R.drawable.ic_language)
        }
        binding.languageButton.setOnClickListener {
            showLanguageDialogIf {
                true
            }
        }
    }

    private fun openNotePad(gameTypeModel: GameTypeModel) {
        notePadLauncher.launch(
            Intent(this, NotePadActivity::class.java).also {
                it.putParcelableArrayListExtra(
                    NotePadActivity.CHARACTER_MODEL_LIST_EXTRA,
                    characterViewModel?.characterArray
                )
                it.putExtra(NotePadActivity.GAME_TYPE_MODEL_EXTRA, gameTypeModel)
            }
        )
    }

    private fun initializeConnectionState() =
        ConnectionHelper.icConnectionAvailable {
            MainApplication.connectionIsEstablished = it
        }

    private fun setupRecyclerView() {
        characterViewModel?.characterArray?.firstOrNull()?.let {
            characterViewModel?.characterArray?.clear()
            characterViewModel?.characterArray?.add(it)
        }
        binding.playersRecyclerView.layoutManager = FlexboxLayoutManager(this@SplashActivity, FlexDirection.ROW).also {
            it.flexDirection = FlexDirection.ROW
            it.justifyContent = if (isLandscape) JustifyContent.FLEX_START else JustifyContent.CENTER
            it.alignItems = AlignItems.CENTER
        }
        binding.playersRecyclerView.adapter = PlayersAdapter(context = this, characterListModel = characterViewModel?.characterArray ?: listOf())
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

    private fun checkGoogleSignIn(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(
                idToken = account?.idToken ?: return
            )
        }
        catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                    initializeGoogleServices()
            }
    }

    private fun initializeReviewManager() {
        if (!sharedPreferences.ratingValue)
            ReviewManager.initialize(
                appCompatActivity = this
            ) {
                sharedPreferences.setRatingValue(
                    value = true
                )
                Toast.makeText(
                    this,
                    resources.getString(R.string.key_thankful_review_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}
