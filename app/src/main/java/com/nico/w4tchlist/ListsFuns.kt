package com.nico.w4tchlist

import android.util.Log
import com.nico.w4tchlist.interfaces.ListInterface
import com.nico.w4tchlist.models.ListResponse
import com.nico.w4tchlist.models.MovieList
import com.nico.w4tchlist.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListsFuns {
    fun createList(session_id : String, createListRequest : MovieList, callback: (Boolean) -> Unit){
        val apiService = MovieApiService.getInstance().create(ListInterface::class.java)
        apiService.createList(session_id, createListRequest).enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>){
                return callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }
}
