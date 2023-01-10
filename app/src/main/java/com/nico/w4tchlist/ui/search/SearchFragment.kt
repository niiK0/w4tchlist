package com.nico.w4tchlist.ui.search

import android.content.Context
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
        val data_funs = GetDataFuns()

        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.setHasFixedSize(true)

        val bundle = requireArguments()
        var search_value = ""

        database.getAdultValue(authManager.auth.currentUser!!.uid)
        Thread{
            database.latch.await()

            val adult = database.getUserAdult()

            if(bundle != null){
                val args = SearchFragmentArgs.fromBundle(bundle)
                search_value = args.searchValue.toString()

                if(args.searchValue != ".null"){
                    data_funs.getMovieData(args.searchValue.toString(), cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                        binding.rvMoviesList.adapter = MovieAdapter(movies)
                        max_Pages = max_pages
                        cur_Page = cur_page
                        binding.tvPages.text = cur_Page.toString() + " / " + max_Pages.toString()
                    }
                }
            }

            binding.btnNext.setOnClickListener{
                if(cur_Page<max_Pages) {
                    cur_Page++
                    data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                        binding.rvMoviesList.adapter = MovieAdapter(movies)
                        max_Pages = max_pages
                        cur_Page = cur_page
                        binding.tvPages.text = cur_Page.toString() + " / " + max_Pages.toString()
                    }
                }else{
                    Toast.makeText(this.context, "You are on the last page.", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnPrevious.setOnClickListener{
                if(cur_Page>1) {
                    cur_Page--
                    data_funs.getMovieData(search_value, cur_Page, adult) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                        binding.rvMoviesList.adapter = MovieAdapter(movies)
                        max_Pages = max_pages
                        cur_Page = cur_page
                        binding.tvPages.text = cur_Page.toString() + " / " + max_Pages.toString()
                    }
                }else{
                    Toast.makeText(this.context, "You are on the first page.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}