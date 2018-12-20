package com.melnykov.mvvmtesting.data.remote

import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.request.LoginRequestBody
import com.melnykov.mvvmtesting.testutil.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var apiService: ApiService

    @Rule
    @JvmField
    val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun loginRequest_IsProperlyExecuted() {
        enqueueResponse("login.json")

        apiService.login(LoginRequestBody("username", "password")).execute()

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(recordedRequest.path, `is`("/login"))

        assertThat(recordedRequest.body.readUtf8(),
            `is`("{\"username\":\"username\",\"password\":\"password\"}"))
    }

    @Test
    fun loginResponse_IsProperlyConverted() {
        enqueueResponse("login.json")

        val response = apiService.login(
            LoginRequestBody("username", "password")).execute()
        val responseBody = response.body()

        assertNotNull(responseBody)
        assertThat(responseBody.accessToken, equalTo("access_token"))
        assertThat(responseBody.user, equalTo(User(
            "makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")))
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        mockWebServer.enqueue(MockResponse()
            .setBody(source.readString(StandardCharsets.UTF_8)))
    }
}