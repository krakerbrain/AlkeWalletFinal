package com.example.alkewalletfinal.integration_test.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alkewalletfinal.api.ApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class ApiServiceIntegrationTest {

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
    fun getUserDetails_shouldReturnUser() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "first_name": "John",
                    "last_name": "Doe",
                    "email": "john.doe@example.com",
                    "password": "password123",
                    "roleId": 1,
                    "points": 1000
                }
            """)

        mockWebServer.enqueue(mockResponse)

        val response = apiService.getUserDetails("Bearer real_token").execute()

        val request = mockWebServer.takeRequest()
        assertEquals("/auth/me", request.path)
        assertEquals("GET", request.method)
        assertEquals("Bearer real_token", request.getHeader("Authorization"))

        val responseBody = response.body()
        assertNotNull(responseBody)
        assertEquals("John", responseBody?.firstName)
        assertEquals("Doe", responseBody?.lastName)
        assertEquals("john.doe@example.com", responseBody?.email)
        assertEquals("password123", responseBody?.password)
        assertEquals(1, responseBody?.roleId)
        assertEquals(1000, responseBody?.points)
    }
}
