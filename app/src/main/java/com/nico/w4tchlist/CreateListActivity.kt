package com.nico.w4tchlist

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nico.w4tchlist.models.MovieList
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager

class CreateListActivity : AppCompatActivity(){
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var privateCheckBox: CheckBox
    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    val database = DatabaseManager()
    val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_create)
        authManager.setContext(this)

        nameEditText = findViewById(R.id.etName)
        descriptionEditText = findViewById(R.id.etDescription)
        privateCheckBox = findViewById(R.id.cbPrivate)
        confirmButton = findViewById(R.id.btnConfirm)
        backButton = findViewById(R.id.btnBack)

        confirmButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val isPrivate = privateCheckBox.isChecked

            database.getAdultValue(authManager.auth.currentUser!!.uid){
                val adult = it
            }
        }

        backButton.setOnClickListener {
            // Finish the activity and return to the fragment
            finish()
        }
    }
}