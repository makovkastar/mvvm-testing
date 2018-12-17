package com.melnykov.mvvmtesting.data.remote.response

import com.google.gson.annotations.SerializedName
import com.melnykov.mvvmtesting.data.model.User

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user") val user: User
)