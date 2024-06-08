package com.example.alkewalletfinal.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Long,
    val concept: String,
    val date: String,
    val type: String,
    val accountID: Long,
    val userID: Long,
    val toAccountID: Long
)
