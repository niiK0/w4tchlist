package com.nico.w4tchlist.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nico.w4tchlist.R
import com.nico.w4tchlist.databinding.FragmentLoginBinding
import com.nico.w4tchlist.services.AuthManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

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
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email field cannot be empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password field cannot be empty"
                return@setOnClickListener
            }

            authManager.login(email, password) { success ->//
                if (success) {
                    Toast.makeText(this.context, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    val navController = findNavController()
                    if(navController.currentDestination?.id != R.id.nav_home){
                        navController.navigate(R.id.nav_home)
                    }
                } else {
                    Toast.makeText(this.context, "There was an error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val navController = findNavController()
            if(navController.currentDestination?.id != R.id.nav_register){
                navController.navigate(R.id.nav_register)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}