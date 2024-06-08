package com.example.alkewalletfinal.model.network

data class AccountResponse(
    val id: Long,
    val creationDate: Any? = null,
    val money: String,
    val isBlocked: Boolean,
    val userID: Long,
    val createdAt: String,
    val updatedAt: String
)
