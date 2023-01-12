package com.nico.w4tchlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val adult: Boolean? = null,
    val movieLists: List<String>? = null,
    val email: String? = null
) : Parcelable