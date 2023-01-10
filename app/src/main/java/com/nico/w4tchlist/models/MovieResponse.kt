package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(
    @SerializedName("results")
    val movies : List<Movie>,
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("page")
    val currentPage : Int

) : Parcelable