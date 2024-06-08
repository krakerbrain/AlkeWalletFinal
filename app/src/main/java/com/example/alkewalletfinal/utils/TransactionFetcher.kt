package com.example.alkewalletfinal.utils

import android.util.Log
import com.example.alkewalletfinal.data.repository.TransactionRepository
import com.example.alkewalletfinal.model.database.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionFetcher(
    private val transactionRepository: TransactionRepository
) {

    fun fetchTransactions(token: String, onComplete: (Boolean, List<TransactionEntity>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val transactionsResponse = transactionRepository.fetchTransactionsFromApi("Bearer $token")
                val transactionsList = transactionsResponse.data.map { response ->
                    TransactionEntity(
                        amount = response.amount,
                        concept = response.concept,
                        date = response.date,
                        type = response.type,
                        accountID = response.accountId,
                        userID = response.userId,
                        toAccountID = response.toAccountId
                    )
                }
                transactionRepository.deleteAllTransactions()
                transactionRepository.saveTransactions(transactionsList)
                onComplete(true, transactionsList)
            } catch (e: Exception) {
                Log.e("TransactionFetcher", "Error fetching transactions", e)
                onComplete(false, null)
            }
        }
    }
}
