package com.nico.w4tchlist.ui.login

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                    createSimpleNotification(this.requireContext(), "Login", "You have successfully signed in!")
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSimpleNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a unique ID for the notification channel
        val channelId = "my_channel_id"

        // Create a notification channel (for devices running Android O or later)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = Notification.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_menu_person)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(0, builder.build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}