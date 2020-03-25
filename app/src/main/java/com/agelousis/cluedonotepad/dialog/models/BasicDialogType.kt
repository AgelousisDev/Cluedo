package com.agelousis.cluedonotepad.dialog.models

import com.agelousis.cluedonotepad.splash.presenters.CharacterPresenter

typealias BasicDialogButtonBlock = () -> Unit
data class BasicDialogType(val basicDialogTypeEnum: BasicDialogTypeEnum = BasicDialogTypeEnum.INFORMATION,
                           val headerBackgroundColor: Int? = null, val title: String? = null, val text: String? = null,
                           val icon: Int? = null, val basicDialogButtonBlock: BasicDialogButtonBlock? = null,
                           val characterPresenter: CharacterPresenter? = null)

enum class BasicDialogTypeEnum {
    INFORMATION, CHARACTER_SELECT;

    var characterPosition: Int? = null

}