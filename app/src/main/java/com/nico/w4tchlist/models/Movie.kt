package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id : Int? = null,
    val title : String? = null,
    val adult : Boolean? = null,

    val genres: List<Genre>? = null,
    val genres_ids: List<Genre>? = null,

    @SerializedName("overview")
    val description : String? = null,

    @SerializedName("poster_path")
    val poster : String? = null,

    @SerializedName("backdrop_path")
    val backdrop : String? = null,

    @SerializedName("vote_average")
    val rate : String? = null,

    @SerializedName("release_date")
    val release : String? = null,

    @SerializedName("production_companies")
    val companies : List<ProdCompany>? = null,

    @SerializedName("spoken_languages")
    val languages : List<Language>? = null
    ) : Parcelable
