package com.agelousis.cluedonotepad.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageModel
import com.agelousis.cluedonotepad.network.repositories.FirebaseMessageRepository
import com.agelousis.cluedonotepad.network.responses.ErrorModel

class NotePadViewModel: ViewModel() {

    private val firebaseResponseLiveData by lazy { MutableLiveData<String?>() }
    private val firebaseErrorLiveData by lazy { MutableLiveData<ErrorModel>() }

    fun sendFirebaseToken(firebaseMessageModel: FirebaseMessageModel) {
        FirebaseMessageRepository.sendFirebaseMessage(
            firebaseMessageModel = firebaseMessageModel,
            onSuccess = {
                firebaseResponseLiveData.value = it
            },
            onFail = {
                firebaseErrorLiveData.value = it
            }
        )
    }

}