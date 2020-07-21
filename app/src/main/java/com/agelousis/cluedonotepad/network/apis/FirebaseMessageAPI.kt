package com.agelousis.cluedonotepad.network.apis

import com.agelousis.cluedonotepad.firebase.models.FirebaseMessageModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FirebaseMessageAPI {

    @POST(value = "https://fcm.googleapis.com/fcm/send")
    fun sendFirebaseMessage(
        @Header(value = "Authorization") authorization: String = "AAAA9dKsbNY:APA91bH2vMXMP5FIsnAsECGAruaet1E1cYfcijiv8wl4NJrNXcyZGdkJEvxoFOZVNnAHcWl8ezBUSFAF_UxnOCZS-g_z6-t1GBSBWM3dSAMYlfHpb506OkFbOaK1OmRlK0YvL1sJExw_",
        @Header(value = "Content-Type") contentType: String = "application/json",
        @Body firebaseMessageModel: FirebaseMessageModel
    ): Call<String?>

}