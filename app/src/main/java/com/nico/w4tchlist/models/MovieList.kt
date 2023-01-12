package com.nico.w4tchlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieList(
    val name : String? = null,
    val movie_count : Int? = null,
    val movies : List<Movie>? = null
) : Parcelable
