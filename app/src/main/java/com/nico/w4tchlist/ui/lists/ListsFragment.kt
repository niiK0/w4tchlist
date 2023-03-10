package com.nico.w4tchlist.ui.lists

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.nico.w4tchlist.*
import com.nico.w4tchlist.databinding.FragmentListsBinding
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.models.MovieList
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager

class ListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val database = DatabaseManager()
    val authManager = AuthManager()

    private lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        authManager.setContext(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val listsViewModel =
            ViewModelProvider(this).get(ListsViewModel::class.java)

        _binding = FragmentListsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListsList.layoutManager = LinearLayoutManager(this.context)
        binding.rvListsList.setHasFixedSize(true)

        val fab = binding.addList

        var userMovieList = mutableListOf<String>()
        var movieLists = mutableListOf<MovieList>()
        var movieListsLid = mutableListOf<String>()

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_lists)
        swipeRefreshLayout.setOnRefreshListener {
            reloadFragment()
        }

        //get user's lists from db
        database.getUserMovieList(authManager.auth.currentUser!!.uid){ userMovieLists ->
            if(userMovieLists != null){
                if(userMovieLists[0] != ""){
                    for(list in userMovieLists){
                        userMovieList.add(list)
                    }

        //          get movelists from the user's list
                    for(list in userMovieList){
                        database.getMovieList(list) { movieList ->
                            if(movieList != null) {
                                movieLists.add(movieList)
                                movieListsLid.add(list)
                                if(list == userMovieList[userMovieList.size-1])
                                    doEverything(movieLists, movieListsLid)
                            }
                        }
                    }
                }
            }
        }

        // Set an OnClickListener on the FAB to start the AddItemActivity
        fab.setOnClickListener {
            val intent = Intent(requireContext(), CreateListActivity::class.java)
            newListLauncher.launch(intent)
        }

    }
    val newListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            Toast.makeText(
                context,
                "List created, slide down to refresh",
                Toast.LENGTH_SHORT
            ).show()
            reloadFragment()
        }
    }

    fun doEverything(movieLists : List<MovieList>, lid: List<String>){
        val adapter = ListsAdapter(movieLists)
        binding.rvListsList.adapter = adapter

        adapter.setOnItemClickListener(object :
            ListsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val navController = findNavController()
                val directions =
                    ListsFragmentDirections.actionListsToListOpen(
                        movieLists[position],
                        lid[position]
                    )
                Log.d("TAG", "LIST ID: ${lid[position]} WITH NAME ${movieLists[position].name}")
                navController.navigate(directions)
            }
        })

        adapter.setOnItemClickListener(object :
            ListsAdapter.onItemLongClickListener {
            override fun onItemLongClick(position: Int): Boolean {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete list")
                builder.setMessage("Are you sure you want to delete this list?")
                builder.setPositiveButton("Delete") { _, _ ->
                    database.deleteList(lid[position]) {
                        database.getUserMovieList(authManager.auth.currentUser!!.uid) { userMovieList ->
                            val newUserMovieList =
                                userMovieList!!.toMutableList()
                            newUserMovieList.remove(lid[position])
                            database.updateUserMovieList(
                                authManager.auth.currentUser!!.uid,
                                newUserMovieList
                            ) {
                                Toast.makeText(
                                    context,
                                    "List deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val navController = findNavController()
                                val directions = ListsFragmentDirections.actionNavListsSelf()
                                navController.navigate(directions)
                            }
                        }
                    }
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show().window?.setBackgroundDrawableResource(R.color.grey)
                return true
            }

        })
    }

    private fun reloadFragment() {
        Log.d("TAG", "refreshing fragment")
        swipeRefreshLayout.isRefreshing = false
        val navController = findNavController()
        val directions = ListsFragmentDirections.actionNavListsSelf()
        navController.navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}