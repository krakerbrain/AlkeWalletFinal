package com.example.alkewalletfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.model.network.DepositOrTransferRequest
import com.example.alkewalletfinal.model.network.DepositOrTransferResponse
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _transactionResult = MutableLiveData<Boolean>()
    val transactionResult: LiveData<Boolean> get() = _transactionResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun depositarOtransferir(type: String, concept: String, amount: Long) {
        viewModelScope.launch {
            val token = sharedPreferencesManager.getAuthToken()
            val accountId = sharedPreferencesManager.getAccountId()
            val depositRequest = DepositOrTransferRequest(type, concept, amount)

            if (token != null && accountId != null) {
                ApiClient.apiService.depositarOtransferir(
                    "Bearer $token",
                    accountId,
                    depositRequest
                ).enqueue(object : Callback<DepositOrTransferResponse> {
                    override fun onResponse(
                        call: Call<DepositOrTransferResponse>,
                        response: Response<DepositOrTransferResponse>
                    ) {
                        if (response.isSuccessful) {
                            _transactionResult.value = true
//                            viewModelScope.launch {
//                                fetchTransactions(token)
//                            }
                        } else {
                            _transactionResult.value = false
                            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                            _errorMessage.value = response.errorBody()?.string() ?: "Unknown error"
                            Log.e("TransactionViewModel", "Error en el depósito: $errorMsg")
                        }
                    }

                    override fun onFailure(call: Call<DepositOrTransferResponse>, t: Throwable) {
                        _transactionResult.value = false
                        _errorMessage.value = t.message ?: "Unknown error"
                        Log.e(
                            "TransactionViewModel",
                            "Error en el depósito: ${t.message ?: "Unknown error"}"
                        )
                    }
                })
            } else {
                _transactionResult.value = false
                Log.e("TransactionViewModel", "Token is null")
            }
        }
    }
}