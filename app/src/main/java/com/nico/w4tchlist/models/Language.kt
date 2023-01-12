package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    @SerializedName("english_name")
    val name : String? = null
) : Parcelable
