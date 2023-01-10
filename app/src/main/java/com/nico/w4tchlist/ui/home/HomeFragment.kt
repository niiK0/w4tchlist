package com.nico.w4tchlist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nico.w4tchlist.GetDataFuns
import com.nico.w4tchlist.MovieAdapter
import com.nico.w4tchlist.databinding.FragmentHomeBinding
import com.nico.w4tchlist.models.Movie

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var max_Pages : Int = 1
    private var cur_Page : Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data_funs = GetDataFuns()

        binding.rvMoviesList.layoutManager = LinearLayoutManager(this.context)
        binding.rvMoviesList.setHasFixedSize(true)

        data_funs.getMovieTrendingData(1){ movies : List<Movie>, cur_page : Int, max_pages : Int ->
            binding.rvMoviesList.adapter = MovieAdapter(movies)
            max_Pages = max_pages
            cur_Page = cur_page
            binding.tvPages.text = cur_page.toString() + " / " + max_pages.toString()
        }

        binding.btnNext.setOnClickListener{
            if(cur_Page<max_Pages) {
                cur_Page++
                data_funs.getMovieTrendingData(cur_Page) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
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
                data_funs.getMovieTrendingData(cur_Page) { movies: List<Movie>, cur_page: Int, max_pages: Int ->
                    binding.rvMoviesList.adapter = MovieAdapter(movies)
                    max_Pages = max_pages
                    cur_Page = cur_page
                    binding.tvPages.text = cur_Page.toString() + " / " + max_Pages.toString()
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