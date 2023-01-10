package com.nico.w4tchlist.models

data class User(
    val adult: Boolean? = false,
    val sessionId: String?
)