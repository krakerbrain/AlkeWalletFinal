package com.example.alkewalletfinal.model.network

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val points: Int
)

