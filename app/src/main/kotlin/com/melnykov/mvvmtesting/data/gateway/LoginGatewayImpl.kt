package com.melnykov.mvvmtesting.data.gateway

import com.melnykov.mvvmtesting.data.executor.disk
import com.melnykov.mvvmtesting.data.executor.network
import com.melnykov.mvvmtesting.data.executor.ui
import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequest
import com.melnykov.mvvmtesting.data.remote.response.LoginResponse
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class LoginGatewayImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : LoginGateway {

    override fun login(username: String, password: String, callbacks: LoginGateway.LoginCallbacks) {
        network {
            try {
                val loginResponse: Response<LoginResponse> = apiService.login(
                    LoginRequest(username, password)).execute()
                if (loginResponse.isSuccessful) {
                    val responseBody = loginResponse.body()
                    if (responseBody != null) {
                        disk {
                            saveAccessToken(responseBody.accessToken)
                            saveUser(responseBody.user)
                            notifyOnLoginSuccess(callbacks)
                        }
                    } else {
                        notifyOnLoginError(callbacks)
                    }
                } else {
                    notifyOnLoginError(callbacks)
                }
            } catch (e: IOException) {
                notifyOnLoginError(callbacks)
                e.printStackTrace()
            }
        }
    }

    private fun notifyOnLoginSuccess(callbacks: LoginGateway.LoginCallbacks) {
        ui { callbacks.onLoginSuccess() }
    }

    private fun notifyOnLoginError(callbacks: LoginGateway.LoginCallbacks) {
        ui { callbacks.onLoginError() }
    }

    private fun saveAccessToken(accessToken: String) {
    }

    private fun saveUser(user: User) {
        userDao.insert(user)
    }
}
