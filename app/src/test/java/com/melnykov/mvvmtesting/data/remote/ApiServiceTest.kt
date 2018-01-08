package com.melnykov.mvvmtesting.data.remote

import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.request.LoginRequest
import com.melnykov.mvvmtesting.data.remote.response.LoginResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var apiService: ApiService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun login() {
        enqueueResponse("login.json")

        val response: Response<LoginResponse> = apiService.login(
                LoginRequest("username", "password")).execute()
        val loginResponse: LoginResponse? = response.body()

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(recordedRequest.path, `is`("/login"))

        assertThat(recordedRequest.body.readUtf8(),
                `is`("{\"username\":\"username\",\"password\":\"password\"}"))

        assertThat(response.isSuccessful, `is`(true))

        assertThat(loginResponse, notNullValue())
        assertThat(loginResponse!!.accessToken, `is`("access_token"))
        assertThat(loginResponse.user, equalTo(User(
                "makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")))
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader.getResourceAsStream(
                "api-response/" + fileName)
        val source = Okio.buffer(Okio.source(inputStream))
        mockWebServer.enqueue(MockResponse()
                .setBody(source.readString(StandardCharsets.UTF_8)))
    }
}