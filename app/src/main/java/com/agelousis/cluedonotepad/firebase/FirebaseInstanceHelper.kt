package com.agelousis.cluedonotepad.firebase

import com.google.firebase.iid.FirebaseInstanceId

typealias FirebaseTokenSuccessBlock = (token: String) -> Unit
class FirebaseInstanceHelper {

    companion object {
        val shared = FirebaseInstanceHelper()
    }

    fun initializeFirebaseToken(firebaseTokenSuccessBlock: FirebaseTokenSuccessBlock) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            firebaseTokenSuccessBlock(it.token)
        }
    }

}