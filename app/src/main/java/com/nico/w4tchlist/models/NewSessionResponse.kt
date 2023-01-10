package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewSessionResponse(
    val success : Boolean,
    @SerializedName("session_id")
    val session_id: String
) : Parcelable
