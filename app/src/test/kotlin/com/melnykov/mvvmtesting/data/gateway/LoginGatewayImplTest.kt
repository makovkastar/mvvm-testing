package com.melnykov.mvvmtesting.data.gateway

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequestBody
import com.melnykov.mvvmtesting.data.remote.response.LoginResponseBody
import com.melnykov.mvvmtesting.testutil.any
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.net.ConnectException

@RunWith(MockitoJUnitRunner::class)
class LoginGatewayImplTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var call: Call<LoginResponseBody>

    private lateinit var loginGateway: LoginGatewayImpl

    private val user = User("makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")

    @Before
    fun setUp() {
        loginGateway = LoginGatewayImpl(apiService, userDao,
            Dispatchers.Unconfined)
    }

    @Test
    fun login_ExecutesCorrectApiCall() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(
            LoginResponseBody("access_token", user)))

        loginGateway.login("username", "password")

        verify(apiService).login(LoginRequestBody("username", "password"))
        verify(call).execute()

        verifyNoMoreInteractions(apiService)
        verifyNoMoreInteractions(call)
    }

    @Test
    fun loginSuccess_ReturnsSuccessResult() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(LoginResponseBody(
            "access_token", user)))

        val result = loginGateway.login("username", "password")

        assertThat(result, instanceOf(LoginResult.Success::class.java))
    }

    @Test
    fun loginSuccess_InsertsUserIntoDatabase() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(LoginResponseBody(
            "access_token", user)))

        loginGateway.login("username", "password")

        verify(userDao).insert(user)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun nullResponseBody_ReturnsErrorResult() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(null))

        val result = loginGateway.login("username", "password")

        assertThat(result, instanceOf(LoginResult.Error::class.java))
    }

    @Test
    fun loginInvalidCredentials_ReturnsErrorResult() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.error(401, ResponseBody.create(
            MediaType.parse("application/json"), "Invalid credentials")))

        val result = loginGateway.login("username", "password")

        assertThat(result, instanceOf(LoginResult.Error::class.java))
    }

    @Test
    fun serverConnectionProblem_ReturnsErrorResult() = runBlocking {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenThrow(ConnectException("Failed to connect"))

        val result = loginGateway.login("username", "password")

        assertThat(result, instanceOf(LoginResult.Error::class.java))
    }
}
