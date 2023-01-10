package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewTokenResponse(
    val success : Boolean,
    val expires_at: String,
    @SerializedName("request_token")
    val token: String
) : Parcelable
