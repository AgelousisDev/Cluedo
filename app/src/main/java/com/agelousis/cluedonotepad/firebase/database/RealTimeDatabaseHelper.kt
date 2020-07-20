package com.agelousis.cluedonotepad.firebase.database

import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.firebase.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

typealias UsersSuccessBlock = (List<User>) -> Unit
class RealTimeDatabaseHelper {

    companion object {
        val shared = RealTimeDatabaseHelper()
    }

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

    fun addUser(user: User) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD).push().setValue(user)
    }

    fun getUsers(channel: String, usersSuccessBlock: UsersSuccessBlock) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = arrayListOf<User>()
                snapshot.children.forEach { dataSnapShot ->
                    dataSnapShot.getValue(User::class.java)?.let { user ->
                        if (user.channel == channel)
                            users.add(user)
                    }
                }
                usersSuccessBlock(
                    users
                )
            }
        })
    }

}