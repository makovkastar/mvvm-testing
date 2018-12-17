package com.melnykov.mvvmtesting.data.gateway

import android.support.annotation.MainThread

interface LoginGateway {
    fun login(username: String, password: String, callbacks: LoginCallbacks)

    interface LoginCallbacks {
        @MainThread
        fun onLoginSuccess()

        @MainThread
        fun onLoginError()
    }
}
