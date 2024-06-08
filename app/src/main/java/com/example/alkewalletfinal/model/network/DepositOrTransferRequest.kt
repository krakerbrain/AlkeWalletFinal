package com.example.alkewalletfinal.model.network

data class DepositOrTransferRequest(
    val type: String,
    val concept: String,
    val amount: Long
)
