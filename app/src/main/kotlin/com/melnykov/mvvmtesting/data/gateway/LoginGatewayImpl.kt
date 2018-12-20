package com.melnykov.mvvmtesting.data.gateway

import com.melnykov.mvvmtesting.data.executor.disk
import com.melnykov.mvvmtesting.data.executor.network
import com.melnykov.mvvmtesting.data.executor.ui
import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequestBody
import java.io.IOException
import javax.inject.Inject

class LoginGatewayImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : LoginGateway {

    override fun login(username: String, password: String, callbacks: LoginGateway.LoginCallbacks) {
        network {
            try {
                val response = apiService.login(
                    LoginRequestBody(username, password)).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body()
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
