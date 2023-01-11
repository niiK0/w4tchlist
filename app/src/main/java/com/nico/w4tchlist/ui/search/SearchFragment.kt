package com.nico.w4tchlist.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nico.w4tchlist.GetDataFuns
import com.nico.w4tchlist.MovieActivity
import com.nico.w4tchlist.MovieAdapter
import com.nico.w4tchlist.databinding.FragmentSearchBinding
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.services.AuthManager
import com.nico.w4tchlist.services.DatabaseManager

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

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

    private var max_Pages : Int = 1
    private var cur_Page : Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.setHasFixedSize(true)

        if(authManager.auth.currentUser != null){
            database.getAdultValue(authManager.auth.currentUser!!.uid) {
                val adult = it
                searchMovie(adult)
            }
        }else{
            searchMovie(false)
        }
    }

    fun searchMovie(adult: Boolean){
        val data_funs = GetDataFuns()
        val bundle = requireArguments()
        var search_value = ""

        val args = SearchFragmentArgs.fromBundle(bundle)
        search_value = args.searchValue.toString()

        if(args.searchValue != ".null"){
            data_funs.getMovieData(args.searchValue.toString(), cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                val adapter = MovieAdapter(movies)
                binding.rvMoviesList.adapter = adapter

                max_Pages = max_pages
                cur_Page = cur_page
                val pages_string = "$cur_page / $max_pages"
                binding.tvPages.text = pages_string

                adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                            val intent = Intent(context, MovieActivity::class.java)
                            intent.putExtra("movie", movie)
                            startActivity(intent)
                        }
                    }

                })
            }
        }

        binding.btnNext.setOnClickListener{
            if(cur_Page<max_Pages) {
                cur_Page++
                data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                    val adapter = MovieAdapter(movies)
                    binding.rvMoviesList.adapter = adapter

                    max_Pages = max_pages
                    cur_Page = cur_page
                    val pages_string = "$cur_page / $max_pages"
                    binding.tvPages.text = pages_string

                    adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                                val intent = Intent(context, MovieActivity::class.java)
                                intent.putExtra("movie", movie)
                                startActivity(intent)
                            }
                        }

                    })
                }
            }else{
                Toast.makeText(this.context, "You are on the last page.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPrevious.setOnClickListener{
            if(cur_Page>1) {
                cur_Page--
                data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                    val adapter = MovieAdapter(movies)
                    binding.rvMoviesList.adapter = adapter

                    max_Pages = max_pages
                    cur_Page = cur_page
                    val pages_string = "$cur_page / $max_pages"
                    binding.tvPages.text = pages_string

                    adapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            data_funs.getSpecificMovieData(movies[position].id!!){movie : Movie ->
                                val intent = Intent(context, MovieActivity::class.java)
                                intent.putExtra("movie", movie)
                                startActivity(intent)
                            }
                        }

                    })
                }
            }else{
                Toast.makeText(this.context, "You are on the first page.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}