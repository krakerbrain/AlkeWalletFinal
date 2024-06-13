package com.example.alkewalletfinal.model.network

data class AccountResponse(
    val id: Long,
    val creationDate: Any? = null,
    val money: Double?,
    val isBlocked: Boolean,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)
