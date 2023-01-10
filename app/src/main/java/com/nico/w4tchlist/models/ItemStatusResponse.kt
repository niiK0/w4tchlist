package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemStatusResponse(
    val id: Int,

    @SerializedName("item_present")
    val isItemPresent: Boolean
) : Parcelable
