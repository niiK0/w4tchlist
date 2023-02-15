package com.nico.w4tchlist

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nico.w4tchlist.models.*
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager
import kotlinx.android.synthetic.main.activity_list_owner.*
import java.util.*

class AddOwnerActivity : AppCompatActivity(){
    private lateinit var emailEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    val database = DatabaseManager()
    val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_owner)
        authManager.setContext(this)

        emailEditText = findViewById(R.id.etEmail)
        confirmButton = findViewById(R.id.btnConfirm)
        backButton = findViewById(R.id.btnBack)

        val lid = intent.extras?.getString("lid")!!

        confirmButton.setOnClickListener {
            val email = emailEditText.text.toString()
            var owner_copy = false

            if(email != authManager.auth.currentUser!!.email){
                database.findUserByEmail(email){ userId ->
                    if(userId != null){
                        val uid = userId
                        Log.d("TAG", "LID CHECK 1: $lid")
                        database.getUserMovieList(uid){ userList ->
                            if(userList != null){
                                //check if onwner already exists
                                val newList = userList.toMutableList()

                                for(l_lid in newList){
                                    if(l_lid == lid){
                                        owner_copy = true
                                        Toast.makeText(this, "Target is already an owner!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                if(!owner_copy){
                                    if(newList[0] == ""){
                                        newList[0] = lid
                                    }else{
                                        newList.add(lid)
                                    }
                                    Log.d("TAG", "LID CHECK 2: $lid")
                                    for(test in newList){
                                        Log.d("TAG", "TEST LIST: $test")
                                    }
                                    database.updateUserMovieList(uid, newList){
                                        Toast.makeText(this, "User added successfully.", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else{
                emailEditText.error = "You can't add yourself!"
            }

        }
        backButton.setOnClickListener {
            // Finish the activity and return to the fragment
            finish()
        }
    }
}