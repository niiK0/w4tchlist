package com.nico.w4tchlist.interfaces

import com.nico.w4tchlist.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ListInterface {
        @POST("/3/list?api_key=718b47ffeddfe88efd8e9873b2fd207a")
    fun createList(
        @Query("session_id") sessionId: String,
        @Body createListRequest: MovieList
    ): Call<ListResponse>

//    @POST("/3/list/{list_id}/account_states?api_key=718b47ffeddfe88efd8e9873b2fd207a")
//    fun updateListAccess(
//        @Path("list_id") listId: Int,
//        @Query("session_id") sessionId: String,
//        @Body updateListAccessRequest: UpdateListAccessRequest
//    ): Call<AccountStateResponse>
//
//    @POST("/3/list/{list_id}/item_status?api_key=718b47ffeddfe88efd8e9873b2fd207a")
//    fun addItemToList(
//        @Path("list_id") listId: Int,
//        @Query("session_id") sessionId: String,
//        @Body addItemToListRequest: AddItemToListRequest
//    ): Call<ItemStatusResponse>
}