package com.example.alkewalletfinal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletfinal.api.ApiClient
import com.example.alkewalletfinal.model.network.RegisterRequest
import com.example.alkewalletfinal.model.network.RegisterResponse
import com.example.alkewalletfinal.model.network.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult


    fun signUp(user: User) {

        viewModelScope.launch {
            val registerRequest =
                RegisterRequest(user.firstName, user.lastName, user.email, user.password, 1, 1000)
            ApiClient.apiService.registerUser(registerRequest)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        _registerResult.value = response.isSuccessful
                    }

                    override fun onFailure(p0: Call<RegisterResponse>, t: Throwable) {
                        _registerResult.value = false
                    }

                })
        }
    }


}