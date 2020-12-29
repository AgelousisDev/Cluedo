package com.agelousis.cluedonotepad.notificationDataViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.databinding.NotificationDataViewerDialogFragmentLayoutBinding
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageDataModel

class NotificationDataViewerDialogFragment: DialogFragment() {

    companion object {

        private const val FIREBASE_NOTIFICATION_DATA_MODEL_EXTRA = "NotificationDataViewerDialogFragment=firebaseNotificationDataModelExtra"

        fun show(supportFragmentManager: FragmentManager, firebaseMessageDataModel: FirebaseMessageDataModel) {
            NotificationDataViewerDialogFragment().also {
                it.arguments = with(Bundle()) {
                    putParcelable(FIREBASE_NOTIFICATION_DATA_MODEL_EXTRA, firebaseMessageDataModel)
                    this
                }
                it.retainInstance = true
            }.show(supportFragmentManager, Constants.NOTIFICATION_DATA_VIEWER_DIALOG_TAG)
        }
    }

    private val firebaseNotificationDataModel by lazy { arguments?.getParcelable<FirebaseMessageDataModel>(FIREBASE_NOTIFICATION_DATA_MODEL_EXTRA) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return NotificationDataViewerDialogFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        ).also {
            it.firebaseMessageDataModel = firebaseNotificationDataModel
        }.root
    }

}