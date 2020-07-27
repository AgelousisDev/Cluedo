package com.agelousis.cluedonotepad.firebase.database

import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.firebase.models.User
import com.google.firebase.database.*

typealias UsersSuccessBlock = (List<User>) -> Unit
typealias SearchBlock = (isAvailable: Boolean) -> Unit
class RealTimeDatabaseHelper {

    companion object {
        val shared = RealTimeDatabaseHelper()
    }

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

    fun addUser(user: User) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD).push().setValue(user)
    }

    fun getUsers(channel: String, usersSuccessBlock: UsersSuccessBlock) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                    usersSuccessBlock(
                        users.filter { it.channel == channel }
                    )
                }
        })
    }

    fun deleteChannel(channel: String) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD).orderByChild(Constants.DATABASE_CHANNEL_FIELD).equalTo(channel)
            .addChildEventListener(object: ChildEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.ref.removeValue()
                }
            })
    }

    fun searchChannel(channel: String, searchBlock: SearchBlock) {
        databaseReference.child(Constants.DATABASE_USERS_CHILD)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                    searchBlock(users.any { it.channel == channel })
                }
            })
    }

}