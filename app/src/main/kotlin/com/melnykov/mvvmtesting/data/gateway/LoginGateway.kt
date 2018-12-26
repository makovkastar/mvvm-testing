package com.melnykov.mvvmtesting.data.gateway

interface LoginGateway {
    suspend fun login(username: String, password: String): LoginResult
}

sealed class LoginResult {
    object Success : LoginResult()
    object Error : LoginResult()
}
