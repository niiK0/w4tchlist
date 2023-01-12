package com.nico.w4tchlist.ui.lists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nico.w4tchlist.*
import com.nico.w4tchlist.databinding.FragmentListOpenBinding
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.models.MovieList
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager

class ListOpenFragment : Fragment() {

    private var _binding: FragmentListOpenBinding? = null

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
            ViewModelProvider(this).get(ListOpenViewModel::class.java)

        _binding = FragmentListOpenBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.layoutManager = GridLayoutManager(this.context, 5)
        binding.rvMoviesList.setHasFixedSize(true)

        val bundle = requireArguments()

        val fab = binding.addList

        val args = ListOpenFragmentArgs.fromBundle(bundle)
        val movieList : MovieList = args.movieList as MovieList
        val movieLid : String = args.lid

        if(movieList.movies!![0].id != 0){
            val adapter = MovieAdapterList(movieList.movies!!)
            binding.rvMoviesList.adapter = adapter

            adapter.setOnItemClickListener(object: MovieAdapterList.onItemClickListener{
                override fun onItemClick(position: Int) {
//                //open movie? no time to make
                }
            })

            adapter.setOnItemClickListener(object: MovieAdapterList.onItemLongClickListener{
                override fun onItemLongClick(position: Int): Boolean {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Delete movie")
                    builder.setMessage("Are you sure you want to delete this movie?")
                    builder.setPositiveButton("Delete") { _, _ ->
                        val newList = movieList.movies.toMutableList()
                        newList.removeAt(position)
                        if(newList.isEmpty()){
                            database.deleteList(movieLid){
                                database.getUserMovieList(authManager.auth.currentUser!!.uid){ userMovieList ->
                                    if(userMovieList != null){
                                        val newUserMovieList = userMovieList.toMutableList()
                                        newUserMovieList.remove(movieLid)
                                        database.updateUserMovieList(authManager.auth.currentUser!!.uid, newUserMovieList){
                                            Toast.makeText(context, "List deleted", Toast.LENGTH_SHORT).show()
                                            val navController = findNavController()
                                            navController.navigate(R.id.nav_lists)
                                        }
                                    }
                                }
                            }
                        }else{
                            database.updateListMovies(movieLid, newList){
                                Toast.makeText(context, "Movie successfully deleted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    builder.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.show()
                    return true
                }
            })
        }

        // Set an OnClickListener on the FAB to start the AddItemActivity
        fab.setOnClickListener {
            val popup = PopupMenu(context, fab, R.style.PopupMenu)
            popup.inflate(R.menu.menu_list_movie_add)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_list_movie_add_movie -> {
                        val intent = Intent(context, AddMovieActivity::class.java)
                        intent.putExtra("lid", movieLid)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_list_movie_add_owner -> {
                        val intent = Intent(context, AddOwnerActivity::class.java)
                        intent.putExtra("lid", movieLid)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}