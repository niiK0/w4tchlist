package com.nico.w4tchlist.ui.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nico.w4tchlist.MovieAdapter
import com.nico.w4tchlist.R
import com.nico.w4tchlist.SessionsFuns
import com.nico.w4tchlist.databinding.FragmentRegisterBinding
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.models.UserSession
import com.nico.w4tchlist.services.AuthManager

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val authManager = AuthManager()

    private lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        authManager.setContext(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel =
            ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.etUsername.error = "Username field cannot be empty"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.etEmail.error = "Email field cannot be empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password field cannot be empty"
                return@setOnClickListener
            }

            authManager.register(username, email, password) { success ->//
                if (success) {
                    Toast.makeText(this.context, "Successfully registered", Toast.LENGTH_SHORT).show()

                    val navController = findNavController()
                    if(navController.currentDestination?.id != R.id.nav_home){
                        navController.navigate(R.id.nav_home)
                    }
                } else {
                    Toast.makeText(this.context, "There was an error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}