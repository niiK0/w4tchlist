package com.nico.w4tchlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.nico.w4tchlist.databinding.ActivityMainBinding
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.ui.search.SearchFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var search_view: androidx.appcompat.widget.SearchView
    val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_genres_select, R.id.nav_login, R.id.nav_register, R.id.nav_account, R.id.nav_lists, R.id.nav_account, R.id.nav_lists
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        authManager.setContext(this)

        val sign_out = navView.menu.findItem(R.id.nav_signout)
        sign_out.setOnMenuItemClickListener {
            showLogOutDialog()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        authManager.updateFirstTime()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val action_search : MenuItem = menu.findItem(R.id.action_search)
        search_view = action_search.actionView as androidx.appcompat.widget.SearchView
        search_view.queryHint = "Search.."

        action_search.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                if(navController.currentDestination?.id != R.id.nav_search){
                    navController.navigate(R.id.nav_search)
                }
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                if(navController.currentDestination?.id != R.id.nav_home){
                    navController.navigate(R.id.nav_home)
                }
                return true
            }
        })

        search_view.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                val directions = SearchFragmentDirections.actionAnyToSecond(query.toString())
                navController.navigate(directions)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showLogOutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { _, _ ->
            authManager.logout()
        }
        builder.setNegativeButton("Cancel") { _, _ ->
            // Do nothing
        }
        val dialog = builder.create()
        dialog.show()
    }
}
