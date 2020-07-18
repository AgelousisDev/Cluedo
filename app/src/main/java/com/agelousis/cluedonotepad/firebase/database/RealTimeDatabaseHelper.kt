package com.agelousis.cluedonotepad.firebase.database

import com.agelousis.cluedonotepad.firebase.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RealTimeDatabaseHelper {

    companion object {
        val shared = RealTimeDatabaseHelper()
    }

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

    fun addUser(user: User) {
        databaseReference.child("Users").push().setValue(user)
    }

}