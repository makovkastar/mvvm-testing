package com.melnykov.mvvmtesting.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("access_token") val accessToken: String)