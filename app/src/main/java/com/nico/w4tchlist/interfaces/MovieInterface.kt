package com.nico.w4tchlist.interfaces

import com.nico.w4tchlist.models.GenreResponse
import com.nico.w4tchlist.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieInterface{
    @GET("/3/discover/movie?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun getMovieList(
        @Query("with_genres") query: Int,
        @Query("page") page: Int,
        @Query("adult") adult: Boolean
    ) : Call<MovieResponse>

    @GET("/3/genre/movie/list?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun getGenresList() : Call<GenreResponse>

    @GET("/3/search/movie?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun getMovieList(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") adult: Boolean
    ) : Call<MovieResponse>

    @GET("/3/movie/popular?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun getMovieList(
        @Query("page") page: Int
    ) : Call<MovieResponse>

}