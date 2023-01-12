package com.nico.w4tchlist.services

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.nico.w4tchlist.R

class AuthManager {
    val auth = FirebaseAuth.getInstance()
    val database = DatabaseManager()

    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    fun register(username: String, email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserUsername(username)
                    database.createUser(auth.currentUser!!.uid, email){
                        if(!it){
                            Log.w("TAG", "There was an error writing to database.")
                        }
                    }
                    updateUI()
                    callback(true)
                } else {
                    // Registration failed, display a message to the user
                    callback(false)
                }
            }
    }

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUI()
                    callback(true)
                } else {
                    // Login failed, display a message to the user
                    callback(false)
                }
            }
    }

    fun updateUserUsername(username: String){
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        auth.currentUser?.updateProfile(profileUpdate)?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                updateUI()
                Log.d("username update", "Username updated")
            }
        }
    }

    fun updateUserImage(image: Uri){
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setPhotoUri(image)
            .build()

        auth.currentUser?.updateProfile(profileUpdate)?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                updateUI()
                Log.d("username update", "Username updated")
            }
        }
    }

    fun updateUserPassword(password: String){
        auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI()
                Log.d("password update", "password updated.")
            }
        }
    }

    fun updateUserEmail(email: String){
        auth.currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI()
                Log.d("email update", "email updated.")
            }
        }
    }

    fun updateFirstTime(){
        if(isUserAuthenticated()){
            updateUI()
        }
    }

    fun updateUI() {
        val activity = context as AppCompatActivity
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        val navHeader = navView.getHeaderView(0)
        val imageUri = auth.currentUser?.photoUrl
        if (isUserAuthenticated()) {
            navHeader.findViewById<TextView>(R.id.tvUsername).text = auth.currentUser?.displayName
            navHeader.findViewById<TextView>(R.id.tvUserEmail).text = auth.currentUser?.email
            Glide.with(activity)
                .load(imageUri)
                .placeholder(R.mipmap.ic_profile)
                .into(navHeader.findViewById<ImageView>(R.id.ivProfile))
            navView.menu.setGroupVisible(R.id.menu_guest, false)
            navView.menu.setGroupVisible(R.id.menu_user, true)
        } else {
            navHeader.findViewById<TextView>(R.id.tvUsername).text = context.resources.getString(R.string.nav_header_title)
            navHeader.findViewById<TextView>(R.id.tvUserEmail).text = context.resources.getString(R.string.nav_header_subtitle)
            navHeader.findViewById<ImageView>(R.id.ivProfile).setImageResource(R.mipmap.ic_profile_round)
            navView.menu.setGroupVisible(R.id.menu_guest, true)
            navView.menu.setGroupVisible(R.id.menu_user, false)
        }
    }

    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
        updateUI()
    }
}