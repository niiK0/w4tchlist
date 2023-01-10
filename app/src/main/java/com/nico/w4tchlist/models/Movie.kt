package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    val id : String?,

    @SerializedName("title")
    val title : String?,

    @SerializedName("overview")
    val description : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("vote_average")
    val rate : String?,

    @SerializedName("release_date")
    val release : String?,

    ) : Parcelable
