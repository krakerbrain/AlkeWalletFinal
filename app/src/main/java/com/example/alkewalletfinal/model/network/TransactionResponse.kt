package com.example.alkewalletfinal.model.network

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    val amount: Long,
    val concept: String,
    val date: String,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long
)
