package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountStateResponse(
    val id: Int,
    @SerializedName("favorite")
    val isFavorite: Boolean,
    @SerializedName("rated")
    val rating: Float?,
    @SerializedName("watchlist")
    val isWatchlist: Boolean
) : Parcelable
