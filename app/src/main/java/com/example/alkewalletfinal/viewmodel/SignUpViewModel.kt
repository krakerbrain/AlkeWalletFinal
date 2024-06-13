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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun signUp(user: User) {
        viewModelScope.launch {
            val registerRequest = RegisterRequest(user.firstName, user.lastName, user.email, user.password, 1, 1000)
            ApiClient.apiService.registerUser(registerRequest)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            _registerResult.value = true
                            _errorMessage.value = null
                        } else {
                            // Manejar la respuesta de error
                            try {
                                val errorBody = response.errorBody()?.string()
                                val errorJson = errorBody?.let { JSONObject(it) }
                                val errorMessage = errorJson?.getString("error")
                                if (errorMessage != null) {
                                    if (errorMessage.contains("Duplicate entry")) {
                                        _errorMessage.value = "El correo electrónico ya existe"
                                    } else {
                                        _errorMessage.value = "Registro fallido: $errorMessage"
                                    }
                                }
                            } catch (e: Exception) {
                                _errorMessage.value = "Registro fallido: ${e.message}"
                            }
                            _registerResult.value = false
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        _registerResult.value = false
                        _errorMessage.value = "Registro fallido: ${t.message}"
                    }
                })
        }
    }


}