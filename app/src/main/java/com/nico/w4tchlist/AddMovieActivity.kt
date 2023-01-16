package com.nico.w4tchlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nico.w4tchlist.models.*
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager
import com.nico.w4tchlist.ui.search.SearchFragmentArgs
import kotlinx.android.synthetic.main.activity_list_movie.*
import java.util.*

class AddMovieActivity : AppCompatActivity(){
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var pagesText : TextView
    private lateinit var searchText : EditText
    val database = DatabaseManager()
    val authManager = AuthManager()

    private var max_Pages : Int = 1
    private var cur_Page : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_movie)
        authManager.setContext(this)

        backButton = findViewById(R.id.btnPrevious)
        nextButton = findViewById(R.id.btnNext)
        recyclerView = findViewById(R.id.rvMoviesList)
        pagesText = findViewById(R.id.tvPages)
        searchText = findViewById(R.id.etSearch)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val data_funs = GetDataFuns()
        val lid = intent.extras?.getString("lid")!!

        findViewById<Button>(R.id.btnSearch).setOnClickListener(){
            doEverything(data_funs, lid, searchText.text.toString())
        }
    }

    fun doEverything(data_funs : GetDataFuns, lid: String, search_value : String){

        database.getAdultValue(authManager.auth.currentUser!!.uid) {
            val adult = it

            data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                val adapter = MovieAdapter(movies)
                recyclerView.adapter = adapter

                max_Pages = max_pages
                cur_Page = cur_page
                val pages_string = "$cur_page / $max_pages"
                pagesText.text = pages_string

                adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                            database.getMovieList(lid){ list ->
                                if(list != null){
                                    var get_movies = list.movies!!.toMutableList()
                                    if(list.movies[0].id == 0){
                                        get_movies[0] = movie
                                    }else{
                                        get_movies.add(movie)
                                    }
                                    database.updateListMovieCount(lid, get_movies.size){
                                        database.updateMovieListMovies(lid, get_movies){
                                            Toast.makeText(this@AddMovieActivity, "Movie added successfully.", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                    }
                                }
                            }
                        }
                    }

                })
            }

            nextButton.setOnClickListener{
                if(cur_Page<max_Pages) {
                    cur_Page++
                    data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                        val adapter = MovieAdapter(movies)
                        recyclerView.adapter = adapter

                        max_Pages = max_pages
                        cur_Page = cur_page
                        val pages_string = "$cur_page / $max_pages"
                        pagesText.text = pages_string

                        adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                                    database.getMovieList(lid){ list ->
                                        if(list != null){
                                            var get_movies = list.movies!!.toMutableList()
                                            if(get_movies.isNotEmpty()) {
                                                if(list.movies[0].id == 0){
                                                    get_movies[0] = movie
                                                }else{
                                                    get_movies.add(movie)
                                                }
                                            }
                                            database.updateListMovieCount(lid, get_movies.size){
                                                database.updateMovieListMovies(lid, get_movies){ success ->
                                                    Toast.makeText(this@AddMovieActivity, "Movie added successfully.", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        })
                    }
                }else{
                    Toast.makeText(this, "You are on the last page.", Toast.LENGTH_SHORT).show()
                }
            }

            backButton.setOnClickListener{
                if(cur_Page>1) {
                    cur_Page--
                    data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                        val adapter = MovieAdapter(movies)
                        recyclerView.adapter = adapter

                        max_Pages = max_pages
                        cur_Page = cur_page
                        val pages_string = "$cur_page / $max_pages"
                        pagesText.text = pages_string

                        adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                                    database.getMovieList(lid){ list ->
                                        if(list != null){
                                            var get_movies = list.movies!!.toMutableList()
                                            if(get_movies.isNotEmpty()) {
                                                if(list.movies[0].id == 0){
                                                    get_movies[0] = movie
                                                }else{
                                                    get_movies.add(movie)
                                                }
                                            }
                                            database.updateListMovieCount(lid, get_movies.size){
                                                database.updateMovieListMovies(lid, get_movies){ success ->
                                                    Toast.makeText(this@AddMovieActivity, "Movie added successfully.", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        })
                    }
                }else{
                    Toast.makeText(this, "You are on the first page.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}