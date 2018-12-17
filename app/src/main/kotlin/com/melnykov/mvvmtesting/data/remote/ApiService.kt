package com.melnykov.mvvmtesting.data.remote

import com.melnykov.mvvmtesting.data.remote.request.LoginRequest
import com.melnykov.mvvmtesting.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
