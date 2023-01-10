package com.nico.w4tchlist.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nico.w4tchlist.R
import com.nico.w4tchlist.databinding.FragmentProfileBinding
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val authManager = AuthManager()
    val database = DatabaseManager()

    private lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        authManager.setContext(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        // Load profile picture using Glide library
        Glide.with(this)
            .load(authManager.auth.currentUser?.photoUrl)
            .placeholder(R.mipmap.ic_profile_round)
            .into(binding.ivCard)

        // Set initial values for EditText fields
        binding.tvCardUsername.text = authManager.auth.currentUser?.displayName
        binding.tvCardEmail.text = authManager.auth.currentUser?.email

        binding.etUsername.setText(authManager.auth.currentUser?.displayName)
        binding.etUsername.setTextColor(resources.getColor(R.color.darker_gold))
        binding.etEmail.setText(authManager.auth.currentUser?.email)
        binding.etEmail.setTextColor(resources.getColor(R.color.darker_gold))

        // Disable EditText fields until their respective checkboxes are checked
        binding.etUsername.isEnabled = false
        binding.etEmail.isEnabled = false
        binding.etPassword.isEnabled = false

        // Set checkbox listeners
        binding.cbUsername.setOnCheckedChangeListener { _, isChecked ->
            binding.etUsername.isEnabled = isChecked
            if(isChecked)
                binding.etUsername.setTextColor(resources.getColor(R.color.white))
            else
                binding.etUsername.setTextColor(resources.getColor(R.color.darker_gold))
        }
        binding.cbEmail.setOnCheckedChangeListener { _, isChecked ->
            binding.etEmail.isEnabled = isChecked
            if(isChecked)
                binding.etEmail.setTextColor(resources.getColor(R.color.white))
            else
                binding.etEmail.setTextColor(resources.getColor(R.color.darker_gold))
        }
        binding.cbPassword.setOnCheckedChangeListener { _, isChecked ->
            binding.etPassword.isEnabled = isChecked
            if(isChecked)
                binding.etPassword.setTextColor(resources.getColor(R.color.white))
            else
                binding.etPassword.setTextColor(resources.getColor(R.color.darker_gold))
        }

        // Set stuff for adult checkbox
        database.getAdultValue(authManager.auth.currentUser!!.uid)
        Thread {
            database.latch.await()

            val adult = database.getUserAdult()

            binding.cbAdult.isChecked = adult
        }.start()

        // Set listener for profile picture ImageButton
        binding.ibProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        // Set listener for Save button
        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                saveChanges()
            }
        }
    }

    val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            Glide.with(this)
                .load(result.data?.data)
                .placeholder(R.mipmap.ic_profile)
                .into(binding.ibProfile)
            authManager.updateUserImage(result.data?.data!!)
        }

    }

    private fun validateInput(): Boolean {
        // Validate EditText fields
        if (binding.cbUsername.isChecked && binding.etUsername.text.isEmpty()) {
            binding.etUsername.error = "Username field cannot be empty"
            return false
        }
        if (binding.cbEmail.isChecked && binding.etEmail.text.isEmpty()) {
            binding.etEmail.error = "Email field cannot be empty"
            return false
        }
        if (binding.cbPassword.isChecked && binding.etPassword.text.isEmpty()) {
            binding.etPassword.error = "Password field cannot be empty"
            return false
        }
        return true
    }

    private fun saveChanges() {
        // Update display name
        if (binding.cbUsername.isChecked) {
            val displayName = binding.etUsername.text.toString()
            authManager.updateUserUsername(displayName)
        }

        // Update email address
        if (binding.cbEmail.isChecked) {
            val email = binding.etEmail.text.toString()
            authManager.updateUserEmail(email)
        }

        // Update password
        if (binding.cbPassword.isChecked) {
            val password = binding.etPassword.text.toString()
            authManager.updateUserPassword(password)
        }

        database.updateAdult(binding.cbAdult.isChecked, authManager.auth.currentUser!!.uid)

        Toast.makeText(this.context, "Successfully saved", Toast.LENGTH_SHORT).show()
        val navController = findNavController()
        if(navController.currentDestination?.id != R.id.nav_home){
            navController.navigate(R.id.nav_home)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}