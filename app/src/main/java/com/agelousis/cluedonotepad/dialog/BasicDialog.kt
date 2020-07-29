package com.agelousis.cluedonotepad.dialog

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.adapters.BasicDialogAdapter
import com.agelousis.cluedonotepad.dialog.controller.BasicDialogController
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.presenters.CharacterSelectPresenter
import com.agelousis.cluedonotepad.dialog.presenters.LanguagePresenter
import com.agelousis.cluedonotepad.extensions.savedLanguage
import com.agelousis.cluedonotepad.splash.enumerations.Language
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.basic_dialog_layout.*

class BasicDialog(private val dialogType: BasicDialogType): DialogFragment(), CharacterSelectPresenter, LanguagePresenter {

    companion object {
        fun show(supportFragmentManager: FragmentManager, dialogType: BasicDialogType) {
            with(BasicDialog(dialogType)) {
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.BASIC_DIALOG_TAG)
        }
    }

    private val sharedPreferences by lazy { context?.getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE) }

    override fun onCharacterSelected(characterRowModel: CharacterRowModel) {
        dismiss()
        dialogType.characterPresenter?.onCharacterSelected(
            adapterPosition = dialogType.basicDialogTypeEnum.characterPosition ?: 0,
            characterRowModel = characterRowModel
        )
    }

    override fun onLanguageSelected(language: Language) {
        dismiss()
        sharedPreferences?.savedLanguage = language.locale
        dialogType.languagePresenter?.onLanguageSelected(
            language = language
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return inflater.inflate(R.layout.basic_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
    }

    private fun configureUI() {
        val context = context.takeIf { it != null } ?: return
        basicDialogHeader.background.colorFilter = PorterDuffColorFilter(dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        basicDialogHeaderTitle.text = dialogType.title
        basicDialogHeaderImage.setImageResource(dialogType.icon ?: R.drawable.ic_cluedo)
        when(dialogType.basicDialogTypeEnum) {
            BasicDialogTypeEnum.INFORMATION -> {
                basicDialogRecyclerView.visibility = View.GONE
                basicDialogInstructionsText.text = dialogType.text
                basicDialogOkButton.setBackgroundColor((dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent)))
                basicDialogOkButton.setOnClickListener { dismiss(); dialogType.basicDialogButtonBlock?.invoke() ?: dismiss() }
            }
            BasicDialogTypeEnum.CHARACTER_SELECT -> {
                basicDialogInstructionsText.visibility = View.GONE
                basicDialogOkButton.visibility = View.GONE
                basicDialogRecyclerView.visibility = View.VISIBLE
                configureRecyclerView()
            }
            BasicDialogTypeEnum.LANGUAGE_SELECT -> {
                basicDialogInstructionsText.visibility = View.GONE
                basicDialogRecyclerView.visibility = View.VISIBLE
                basicDialogOkButton.text = resources.getString(R.string.key_cancel_label)
                basicDialogOkButton.setOnClickListener { dismiss() }
                configureRecyclerView()
            }
        }
    }

    private fun configureRecyclerView() {
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        basicDialogRecyclerView.layoutManager = flexLayoutManager
        val adapter = BasicDialogAdapter(
            list = when(dialogType.basicDialogTypeEnum) {
                BasicDialogTypeEnum.CHARACTER_SELECT -> BasicDialogController.getCluedoCharacters(context = context ?: return)
                BasicDialogTypeEnum.LANGUAGE_SELECT -> BasicDialogController.availableLanguages
                else -> return
            },
            characterSelectPresenter = this,
            languagePresenter = this)
        basicDialogRecyclerView.adapter = adapter
    }

}