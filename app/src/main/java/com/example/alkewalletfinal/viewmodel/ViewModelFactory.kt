package com.example.alkewalletfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import com.example.alkewalletfinal.utils.TransactionFetcher

class ViewModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val transactionFetcher: TransactionFetcher
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginViewModel(sharedPreferencesManager, transactionFetcher) as T
            }
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TransactionViewModel(sharedPreferencesManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
