package com.example.alkewalletfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.model.database.TransactionEntity
import com.example.alkewalletfinal.model.network.AccountResponse
import com.example.alkewalletfinal.model.network.CreateAccountResponse
import com.example.alkewalletfinal.model.network.LoginRequest
import com.example.alkewalletfinal.model.network.LoginResponse
import com.example.alkewalletfinal.model.network.User
import com.example.alkewalletfinal.utils.SharedPreferencesManager
import com.example.alkewalletfinal.utils.TransactionFetcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val transactionFetcher: TransactionFetcher? = null
) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _userDetails = MutableLiveData<User>()

    private val _createAccountResult = MutableLiveData<Boolean>()
    val createAccountResult: LiveData<Boolean> get() = _createAccountResult

    private val _updateAccountResult = MutableLiveData<Boolean>()
    val updateAccountResult: LiveData<Boolean> get() = _updateAccountResult

    private val _accountDetailsUpdated = MutableLiveData<Boolean>()
    val accountDetailsUpdated: LiveData<Boolean> get() = _accountDetailsUpdated

    private val transactions = MutableLiveData<List<TransactionEntity>?>()

    fun login(email: String, password: String, isSignUp: Boolean = false) {
        val loginRequest = LoginRequest(email, password)
        ApiClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.accessToken
                    if (token != null) {
                        sharedPreferencesManager.saveAuthToken(token)
                        getUserDetails(token, isSignUp)
                    } else {
                        _loginResult.value = false
                    }
                } else {
                    _loginResult.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResult.value = false
            }
        })
    }

    private fun getUserDetails(token: String, isSignUp: Boolean) {
        ApiClient.apiService.getUserDetails("Bearer $token").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        sharedPreferencesManager.saveUser(it)
                        getUserAccountsDetails(token)
                        _userDetails.value = it

                        if (isSignUp) {
                            createAccount(token)
                        }
                    }
                } else {
                    _loginResult.value = false
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _loginResult.value = false
            }
        })
    }

    fun getUserAccountsDetails(token: String) {
        ApiClient.apiService.getUserAccountsDetails("Bearer $token").enqueue(object : Callback<List<AccountResponse>> {
            override fun onResponse(
                call: Call<List<AccountResponse>>,
                response: Response<List<AccountResponse>>
            ) {
                if (response.isSuccessful) {
                    val accounts = response.body()
                    accounts?.let {
                        if (it.isNotEmpty()) {
                            val account = it[0]
                            sharedPreferencesManager.saveAccountDetails(account.money, account.id)
                            _accountDetailsUpdated.value = true
                        }
                    }
                    fetchTransactions(token)
                } else {
                    _accountDetailsUpdated.value = false
                }
            }

            override fun onFailure(call: Call<List<AccountResponse>>, t: Throwable) {
                _accountDetailsUpdated.value = false
            }
        })
    }

    private fun fetchTransactions(token: String) {
        transactionFetcher?.fetchTransactions(token) { success, transactionsList ->
            if (success) {
                Log.d("Transactions", "Transactions from login: $transactionsList")
                transactions.postValue(transactionsList)
                _loginResult.postValue(true)
            } else {
                _loginResult.postValue(false)
            }
        }
    }

    private fun createAccount(token: String) {
        ApiClient.apiService.createAccount("Bearer $token").enqueue(object : Callback<CreateAccountResponse> {
            override fun onResponse(
                call: Call<CreateAccountResponse>,
                response: Response<CreateAccountResponse>
            ) {
                if (response.isSuccessful) {
                    _createAccountResult.value = true
                    updateAccount(token)
                } else {
                    _createAccountResult.value = false
                }
            }

            override fun onFailure(call: Call<CreateAccountResponse>, t: Throwable) {
                _createAccountResult.value = false
            }
        })
    }

    private fun updateAccount(token: String) {
        val updateData = mapOf(
            "money" to 0.00,
            "isBlocked" to false
        )
        ApiClient.apiService.updateAccount("Bearer $token", updateData).enqueue(object : Callback<CreateAccountResponse> {
            override fun onResponse(
                call: Call<CreateAccountResponse>,
                response: Response<CreateAccountResponse>
            ) {
                if (response.isSuccessful) {
                    _updateAccountResult.value = true
                    sharedPreferencesManager.saveSaldo("0")
                } else {
                    _updateAccountResult.value = false
                }
            }

            override fun onFailure(call: Call<CreateAccountResponse>, t: Throwable) {
                _updateAccountResult.value = false
            }
        })
    }
}
