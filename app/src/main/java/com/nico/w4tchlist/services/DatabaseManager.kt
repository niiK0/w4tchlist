package com.nico.w4tchlist.services

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nico.w4tchlist.models.User
import java.util.concurrent.CountDownLatch
import javax.security.auth.callback.Callback

class DatabaseManager {
    val database = Firebase.database("https://w4tchlist-bb8fd-default-rtdb.europe-west1.firebasedatabase.app/")

    fun createUser(uid: String){
        val ref = database.getReference("Users")
        val user = User(false)
        ref.child(uid).setValue(user)
    }

    fun getAdultValue(uid: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Users").child(uid)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is triggered whenever the data at the reference changes
                if (dataSnapshot.exists()) {
                    // The child node with the given ID exists
                    val user = dataSnapshot.getValue(User::class.java)
                    callback(user?.adult?:false)
                } else {
                    Log.w("TAG", "error on getting field")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // This method is triggered when there is an error accessing the reference
                Log.w("TAG", "checkUserExists:onCancelled", databaseError.toException())
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateAdult(adult: Boolean, uid: String){
        val ref = database.getReference("Users").child(uid)
        ref.child("adult").setValue(adult)
    }
}
