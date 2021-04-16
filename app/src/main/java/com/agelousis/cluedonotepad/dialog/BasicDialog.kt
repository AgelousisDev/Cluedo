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
import com.agelousis.cluedonotepad.databinding.BasicDialogLayoutBinding
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

class BasicDialog(private val dialogType: BasicDialogType): DialogFragment(), CharacterSelectPresenter, LanguagePresenter {

    companion object {
        fun show(supportFragmentManager: FragmentManager, dialogType: BasicDialogType) {
            BasicDialog(dialogType).show(supportFragmentManager, Constants.BASIC_DIALOG_TAG)
        }
    }

    private lateinit var binding: BasicDialogLayoutBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        binding = BasicDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
    }

    private fun configureUI() {
        val context = context.takeIf { it != null } ?: return
        binding.basicDialogHeader.background?.colorFilter = PorterDuffColorFilter(dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        binding.basicDialogHeaderTitle.text = dialogType.title
        binding.basicDialogHeaderImage.setImageResource(dialogType.icon ?: R.drawable.ic_cluedo)
        when(dialogType.basicDialogTypeEnum) {
            BasicDialogTypeEnum.INFORMATION -> {
                binding.basicDialogRecyclerView.visibility = View.GONE
                binding.basicDialogInstructionsText.text = dialogType.text
                binding.basicDialogOkButton.setBackgroundColor((dialogType.headerBackgroundColor ?: ContextCompat.getColor(context, R.color.colorAccent)))
                binding.basicDialogOkButton.setOnClickListener { dismiss(); dialogType.basicDialogButtonBlock?.invoke() ?: dismiss() }
            }
            BasicDialogTypeEnum.CHARACTER_SELECT, BasicDialogTypeEnum.LANGUAGE_SELECT -> {
                binding.basicDialogInstructionsText.visibility = View.GONE
                binding.basicDialogOkButton.visibility = View.GONE
                binding.basicDialogRecyclerView.visibility = View.VISIBLE
                configureRecyclerView()
            }
        }
    }

    private fun configureRecyclerView() {
        val flexLayoutManager = FlexboxLayoutManager(context, FlexDirection.ROW)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        binding.basicDialogRecyclerView.layoutManager = flexLayoutManager
        val adapter = BasicDialogAdapter(
            list = when(dialogType.basicDialogTypeEnum) {
                BasicDialogTypeEnum.CHARACTER_SELECT -> BasicDialogController.getCluedoCharacters(context = context ?: return)
                BasicDialogTypeEnum.LANGUAGE_SELECT -> BasicDialogController.availableLanguages
                else -> return
            },
            characterSelectPresenter = this,
            languagePresenter = this)
        binding.basicDialogRecyclerView.adapter = adapter
    }

}