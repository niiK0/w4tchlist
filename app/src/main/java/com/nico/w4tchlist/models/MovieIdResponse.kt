package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieIdResponse(
    val id : String?,
    val title : String?,
    val adult : Boolean?,
    val genres: List<Genre>?,

    @SerializedName("overview")
    val description : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("backdrop_path")
    val backdrop : String?,

    @SerializedName("vote_average")
    val rate : String?,

    @SerializedName("release_date")
    val release : String?,

    @SerializedName("production_companies")
    val companies : List<ProdCompany>?,

    @SerializedName("spoken_languages")
    val languages : List<Language>?

) : Parcelable