package com.nico.w4tchlist.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProdCompany(
    val id : Int? = null,
    val name : String? = null
) : Parcelable
