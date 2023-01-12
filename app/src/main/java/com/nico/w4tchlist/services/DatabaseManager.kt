package com.nico.w4tchlist.services

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.models.MovieList
import com.nico.w4tchlist.models.User
import java.util.UUID

class DatabaseManager {
    val database = Firebase.database("https://w4tchlist-bb8fd-default-rtdb.europe-west1.firebasedatabase.app/")

    fun createUser(uid: String, email: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Users")
        val user = User(false, mutableListOf(""), email)
        var success = false

        ref.child(uid).setValue(user)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun findUserByEmail(email : String, callback: (String?) -> Unit){
        val ref = database.reference
        val query = ref.child("Users").orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val uid = userSnapshot.key
                        callback(uid)
                    }
                } else {
                    callback(null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // An error occurred
            }
        })
    }

    fun addUserMovieList(uid: String, movieList: List<String>){
        val ref = database.getReference("Users").child(uid)
        ref.child("movieLists").setValue(movieList)
    }

    fun getUserMovieList(uid : String, callback: (List<String>?) -> Unit){
        val ref = database.getReference("Users").child(uid)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is triggered whenever the data at the reference changes
                if (snapshot.exists()) {
                    // The child node with the given ID exists
                    val user = snapshot.getValue(User::class.java)
                    callback(user?.movieLists)
                } else {
                    Log.w("TAG", "error on getting field")
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "checkUserExists:onCancelled", error.toException())
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    fun createList(lid: String, uid : String, list : MovieList, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists")

        var success = false
        ref.child(lid).setValue(list)
            .addOnSuccessListener {
                success = true
                getUserMovieList(uid) {
                    if (it == null) {
                        val newList = mutableListOf<String>()
                        newList.add(lid)
                        addUserMovieList(uid, newList)
                    } else {
                        val newList = it.toMutableList()
                        if (newList[0] == "") {
                            newList[0] = lid
                        } else {
                            newList.add(lid)
                        }
                        addUserMovieList(uid, newList)
                    }
                }
            }

        callback(success)
    }

    fun updateListName(lid: String, new_name: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists").child(lid).child("name")
        var success = false
        ref.setValue(new_name)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun updateListMovieCount(lid: String, new_count: Int, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists").child(lid).child("movie_count")
        var success = false
        ref.setValue(new_count)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun deleteList(lid: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists").child(lid)
        var success = false
        ref.removeValue()
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun updateListMovies(lid: String, new_movies: List<Movie>, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists").child(lid).child("movies")
        var success = false
        ref.setValue(new_movies)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun updateUserMovieList(uid: String, new_list: List<String>, callback: (Boolean) -> Unit){
        val ref = database.getReference("Users").child(uid).child("movieLists")
        var success = false
        ref.setValue(new_list)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun getMovieListsString(callback: (List<String>) -> Unit){
        val ref = database.getReference("Lists")
        val createList = mutableListOf<String>()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.value as? HashMap<String, Any>
                if (hashMap != null) {
                    createList.clear()
                    createList.addAll(hashMap.keys)
                    callback(createList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "checkUserExists:onCancelled", error.toException())
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getMovieList(lid : String, callback: (MovieList?) -> Unit){
        val ref = database.getReference("Lists").child(lid)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is triggered whenever the data at the reference changes
                if (snapshot.exists()) {
                    // The child node with the given ID exists
                    val list = snapshot.getValue(MovieList::class.java)
                    callback(list)
                } else {
                    Log.w("TAG", "error on getting field")
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "checkUserExists:onCancelled", error.toException())
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateMovieListMovies(lid: String, new_movies: List<Movie>, callback: (Boolean) -> Unit){
        val ref = database.getReference("Lists").child(lid).child("movies")
        var success = false

        ref.setValue(new_movies)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }

    fun getAdultValue(uid: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Users").child(uid)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is triggered whenever the data at the reference changes
                if (snapshot.exists()) {
                    // The child node with the given ID exists
                    val user = snapshot.getValue(User::class.java)
                    callback(user?.adult?:false)
                } else {
                    Log.w("TAG", "error on getting field")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "checkUserExists:onCancelled", error.toException())
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateAdult(adult: Boolean, uid: String, callback: (Boolean) -> Unit){
        val ref = database.getReference("Users").child(uid)
        var success = false

        ref.child("adult").setValue(adult)
            .addOnSuccessListener {
                success = true
            }

        callback(success)
    }
}
