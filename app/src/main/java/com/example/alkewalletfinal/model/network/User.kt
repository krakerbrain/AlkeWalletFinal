package com.example.alkewalletfinal.model.network

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val roleId: Int = 1,
    val points: Int = 1000
)
