package com.melnykov.mvvmtesting.data.remote

import com.melnykov.mvvmtesting.data.remote.request.LoginRequestBody
import com.melnykov.mvvmtesting.data.remote.response.LoginResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body requestBody: LoginRequestBody): Call<LoginResponseBody>
}
