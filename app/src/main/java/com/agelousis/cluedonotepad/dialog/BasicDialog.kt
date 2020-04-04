package com.agelousis.cluedonotepad.dialog

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
import com.agelousis.cluedonotepad.dialog.adapters.CharacterAdapter
import com.agelousis.cluedonotepad.dialog.controller.BasicDialogController
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.dialog.models.BasicDialogTypeEnum
import com.agelousis.cluedonotepad.dialog.models.CharacterRowModel
import com.agelousis.cluedonotepad.dialog.presenters.CharacterSelectPresenter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.basic_dialog_layout.*

class BasicDialog(private val dialogType: BasicDialogType): DialogFragment(), CharacterSelectPresenter {

    companion object {
        fun show(supportFragmentManager: FragmentManager, dialogType: BasicDialogType) {
            with(BasicDialog(dialogType)) {
                retainInstance = true
                this
            }.show(supportFragmentManager, Constants.BASIC_DIALOG_TAG)
        }
    }

    override fun onCharacterSelected(characterRowModel: CharacterRowModel) {
        dismiss()
        dialogType.characterPresenter?.onCharacterSelected(adapterPosition = dialogType.basicDialogTypeEnum.characterPosition ?: 0,
            characterRowModel = characterRowModel)
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
                basicDialogOkButton.visibility = View.GONE
                basicDialogRecyclerView.visibility = View.VISIBLE
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
        val adapter = CharacterAdapter(characterModelList = BasicDialogController.getCluedoCharacters(context = context ?: return),
            characterSelectPresenter = this)
        basicDialogRecyclerView.adapter = adapter
    }

}