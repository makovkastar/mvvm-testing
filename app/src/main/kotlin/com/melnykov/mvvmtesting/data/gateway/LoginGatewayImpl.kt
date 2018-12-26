package com.melnykov.mvvmtesting.data.gateway

import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequestBody
import com.melnykov.mvvmtesting.injection.qualifier.BgContext
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginGatewayImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    @BgContext private val bgContext: CoroutineContext
) : LoginGateway {

    override suspend fun login(
        username: String,
        password: String
    ) = withContext(bgContext) {
        try {
            val response = apiService.login(
                LoginRequestBody(username, password)).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    saveAccessToken(responseBody.accessToken)
                    saveUser(responseBody.user)
                    return@withContext LoginResult.Success
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext LoginResult.Error
    }

    private fun saveAccessToken(accessToken: String) {
        // TODO: Persist the access token.
    }

    private fun saveUser(user: User) {
        userDao.insert(user)
    }
}
