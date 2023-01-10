package com.nico.w4tchlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieList(
    val name : String?,
    val description : String?,
    val language : String? = "en",
    val private : Boolean?

) : Parcelable
