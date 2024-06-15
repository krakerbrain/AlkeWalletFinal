package com.example.alkewalletfinal.test.api

import com.example.alkewalletfinal.api.ApiService
import com.example.alkewalletfinal.model.network.LoginRequest
import com.example.alkewalletfinal.model.network.RegisterRequest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login deberia retornar LoginResponse`() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{\"accessToken\":\"fake_token\"}")

        mockWebServer.enqueue(mockResponse)

        val response = apiService.login(LoginRequest("user", "password")).execute()

        val request = mockWebServer.takeRequest()
        assertEquals("/auth/login", request.path)
        assertEquals("POST", request.method)

        val responseBody = response.body()
        assertEquals("fake_token", responseBody?.accessToken)
    }

    @Test
    fun `getUserDetails deberia  retornar User`() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                """
                {
                    "first_name": "John",
                    "last_name": "Doe",
                    "email": "john.doe@example.com",
                    "password": "password123",
                    "roleId": 1,
                    "points": 1000
                }
            """
            )

        mockWebServer.enqueue(mockResponse)

        val response = apiService.getUserDetails("Bearer fake_token").execute()

        val request = mockWebServer.takeRequest()
        assertEquals("/auth/me", request.path)
        assertEquals("GET", request.method)
        assertEquals("Bearer fake_token", request.getHeader("Authorization"))

        val responseBody = response.body()
        assertEquals("John", responseBody?.firstName)
        assertEquals("Doe", responseBody?.lastName)
        assertEquals("john.doe@example.com", responseBody?.email)
        assertEquals("password123", responseBody?.password)
        assertEquals(1, responseBody?.roleId)
        assertEquals(1000, responseBody?.points)
    }

    @Test
    fun `registerUser deberia retornar RegisterResponse`() {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                """
                {
                    "first_name": "John",
                    "last_name": "Doe",
                    "email": "john.doe@example.com",
                    "password": "password123",
                    "roleId": 1,
                    "points": 1000
                }
            """
            )

        mockWebServer.enqueue(mockResponse)

        val response = apiService.registerUser(
            RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123",
                1,
                50
            )
        ).execute()


        val request = mockWebServer.takeRequest()
        assertEquals("/users", request.path)
        assertEquals("POST", request.method)

        val responseBody = response.body()
        assertEquals("John", responseBody?.firstName)
        assertEquals("Doe", responseBody?.lastName)
        assertEquals("john.doe@example.com", responseBody?.email)
    }
}
