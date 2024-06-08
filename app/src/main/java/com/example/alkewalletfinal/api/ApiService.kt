package com.example.alkewalletfinal.api

import com.example.alkewalletfinal.model.network.DepositOrTransferRequest
import com.example.alkewalletfinal.model.network.AccountResponse
import com.example.alkewalletfinal.model.network.CreateAccountResponse
import com.example.alkewalletfinal.model.network.DepositOrTransferResponse
import com.example.alkewalletfinal.model.network.LoginRequest
import com.example.alkewalletfinal.model.network.LoginResponse
import com.example.alkewalletfinal.model.network.PaginatedTransactionResponse
import com.example.alkewalletfinal.model.network.RegisterRequest
import com.example.alkewalletfinal.model.network.RegisterResponse
import com.example.alkewalletfinal.model.network.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/auth/me")
    fun getUserDetails(@Header("Authorization") token: String): Call<User>

    @POST("/users")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/accounts")
    fun createAccount(@Header("Authorization") token: String): Call<CreateAccountResponse>

    @PUT("/accounts/me")
    fun updateAccount(
        @Header("Authorization") token: String,
        @Body updateData: Map<String, Any>
    ): Call<CreateAccountResponse>

    @GET("/accounts/me")
    fun getUserAccountsDetails(@Header("Authorization") token: String): Call<List<AccountResponse>>

    @POST("/accounts/{id}")
    fun depositarOtransferir(
        @Header("Authorization") token: String,
        @Path("id") accountId: Long?,
        @Body request: DepositOrTransferRequest
    ): Call<DepositOrTransferResponse>

    @GET("/transactions")
    suspend fun getTransactions(@Header("Authorization") token: String): PaginatedTransactionResponse
}