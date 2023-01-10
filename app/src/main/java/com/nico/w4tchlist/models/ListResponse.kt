package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListResponse(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("item_count")
    val itemCount: Int,

) : Parcelable