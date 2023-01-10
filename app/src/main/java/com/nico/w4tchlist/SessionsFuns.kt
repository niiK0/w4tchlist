package com.nico.w4tchlist

import android.util.Log
import com.nico.w4tchlist.interfaces.ListInterface
import com.nico.w4tchlist.interfaces.SessionInterface
import com.nico.w4tchlist.models.*
import com.nico.w4tchlist.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionsFuns {
    fun generateNewToken(callback: (String) -> Unit){
        val apiService = MovieApiService.getInstance().create(SessionInterface::class.java)
        apiService.generateNewToken().enqueue(object : Callback<NewTokenResponse> {
            override fun onResponse(call: Call<NewTokenResponse>, response: Response<NewTokenResponse>){
                return callback(response.body()!!.token)
            }

            override fun onFailure(call: Call<NewTokenResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }

    fun createNewSession(token: UserSession, callback: (String) -> Unit){
        val apiService = MovieApiService.getInstance().create(SessionInterface::class.java)
        apiService.createNewSession(token).enqueue(object : Callback<NewSessionResponse> {
            override fun onResponse(call: Call<NewSessionResponse>, response: Response<NewSessionResponse>){
                if (response.isSuccessful) {
                    // Handle the successful response
                } else {
                    Log.e("API Error", "Error code: ${response.code()}")
                    Log.e("API Error", "Error body: ${response.errorBody()}")
                    Log.e("API Error", "Error message: ${response.message()}")
                }

                if(response.body() != null){
                    Log.w("Tag", response.body()!!.session_id)
                    Log.w("Tag", response.body()!!.success.toString())
                }
                    return callback(response.body()!!.session_id)
            }

            override fun onFailure(call: Call<NewSessionResponse>, t: Throwable) {
                Log.d("erro?", "deu erro po")
            }

        })
    }
}
