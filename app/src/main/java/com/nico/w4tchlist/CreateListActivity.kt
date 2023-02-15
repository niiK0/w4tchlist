package com.nico.w4tchlist

import android.app.Activity
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
import java.util.*

class CreateListActivity : AppCompatActivity(){
    private lateinit var nameEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    val database = DatabaseManager()
    val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_create)
        authManager.setContext(this)

        nameEditText = findViewById(R.id.etName)
        confirmButton = findViewById(R.id.btnConfirm)
        backButton = findViewById(R.id.btnBack)

        confirmButton.setOnClickListener {
            val name = nameEditText
            val lid = UUID.randomUUID().toString()

            if(name.text.isEmpty()){
                name.error = "List name cannot be empty"
            }else{
                var movieList = MovieList(
                    name.text.toString(),
                    0,
                    mutableListOf(Movie(0, "", false, mutableListOf(Genre(0, "")), mutableListOf(Genre(0, "")), "", "", "", "", "", mutableListOf(
                        ProdCompany(0, "")
                    ), mutableListOf(Language("")))
                ))


                database.createList(lid, authManager.auth.currentUser!!.uid, movieList) {
                    setResult(Activity.RESULT_OK)
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