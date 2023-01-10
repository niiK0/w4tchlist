package com.nico.w4tchlist.interfaces

import com.nico.w4tchlist.models.*
import retrofit2.Call
import retrofit2.http.*

interface SessionInterface {
    @GET("/3/authentication/token/new?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun generateNewToken(): Call<NewTokenResponse>

    @POST("/3/authentication/session/new?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun createNewSession(
        @Body requestToken : UserSession
    ): Call<NewSessionResponse>
}