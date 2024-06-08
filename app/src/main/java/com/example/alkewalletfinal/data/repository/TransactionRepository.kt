package com.example.alkewalletfinal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.alkewalletfinal.api.ApiService
import com.example.alkewalletfinal.data.dao.TransactionDao
import com.example.alkewalletfinal.model.database.TransactionEntity
import com.example.alkewalletfinal.model.network.PaginatedTransactionResponse
import com.example.alkewalletfinal.model.network.TransactionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val apiService: ApiService,
) {

    suspend fun fetchTransactionsFromApi(token: String): PaginatedTransactionResponse {
        return withContext(Dispatchers.IO) {
            apiService.getTransactions(token)
        }
    }

    suspend fun saveTransactions(transactions: List<TransactionEntity>) {
        transactionDao.insertTransactions(transactions)
        Log.d("TransactionRepository", "Transacciones guardadas en la base de datos")
    }

    suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }
}
