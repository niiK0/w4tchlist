package com.nico.w4tchlist

import android.util.Log
import com.nico.w4tchlist.interfaces.MovieInterface
import com.nico.w4tchlist.models.Genre
import com.nico.w4tchlist.models.GenreResponse
import com.nico.w4tchlist.models.Movie
import com.nico.w4tchlist.models.MovieResponse
import com.nico.w4tchlist.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetDataFuns {
    val apiService = MovieApiService.getInstance().create(MovieInterface::class.java)

    fun getGenres(callback: (List<Genre>) -> Unit){
        apiService.getGenresList().enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>){
                return callback(response.body()!!.genres)
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }

    fun getMovieTrendingData(page: Int, callback: (List<Movie>, Int, Int) -> Unit){
        apiService.getMovieList(page).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>){
                return callback(response.body()!!.movies, response.body()!!.currentPage, response.body()!!.totalPages)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }

    fun getMovieData(searchValue : String, page: Int, adult: Boolean, callback: (List<Movie>, Int, Int) -> Unit){
        apiService.getMovieList(searchValue, page, adult).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>){
                return callback(response.body()!!.movies, response.body()!!.currentPage, response.body()!!.totalPages)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }

    fun getMovieGenreData(genreId : Int, page: Int, adult: Boolean, callback: (List<Movie>, Int, Int) -> Unit){
        apiService.getMovieList(genreId, page, adult).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>){
                return callback(response.body()!!.movies, response.body()!!.currentPage, response.body()!!.totalPages)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }
}