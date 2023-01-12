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

            database.findUserByEmail(email){ userId ->
                if(userId != null){
                    val uid = userId
                    database.getUserMovieList(uid){ userList ->
                        if(userList != null){
                            val newList = userList.toMutableList()
                            if(newList.isNotEmpty()) {
                                if(newList[0] == ""){
                                    newList[0] = lid
                                }else{
                                    newList.add(lid)
                                }
                            }
                            database.updateUserMovieList(uid, newList){
                                Toast.makeText(this, "User added successfully.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }

        backButton.setOnClickListener {
            // Finish the activity and return to the fragment
            finish()
        }
    }
}