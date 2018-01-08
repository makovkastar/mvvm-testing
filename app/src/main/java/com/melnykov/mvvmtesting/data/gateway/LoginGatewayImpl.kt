package com.melnykov.mvvmtesting.data.gateway

import com.melnykov.mvvmtesting.data.executor.network
import com.melnykov.mvvmtesting.data.executor.ui
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequest
import com.melnykov.mvvmtesting.data.remote.response.LoginResponse
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class LoginGatewayImpl @Inject constructor(
        private val apiService: ApiService) : LoginGateway {

    override fun login(username: String, password: String, callbacks: LoginGateway.LoginCallbacks) {
        network {
            try {
                val loginResponse: Response<LoginResponse> = apiService.login(
                        LoginRequest(username, password)).execute()
                if (loginResponse.isSuccessful) {
                    val responseBody = loginResponse.body()
                    if (responseBody != null) {
                        saveAccessToken(responseBody.accessToken)
                    }
                    ui { callbacks.onLoginSuccess() }
                } else {
                    ui { callbacks.onLoginError() }
                }
            } catch (e: IOException) {
                ui {
                    callbacks.onLoginError()
                }
                e.printStackTrace()
            }
        }
    }

    private fun saveAccessToken(accessToken: String) {
    }
}
