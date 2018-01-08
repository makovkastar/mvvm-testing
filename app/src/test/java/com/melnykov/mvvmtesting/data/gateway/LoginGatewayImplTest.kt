package com.melnykov.mvvmtesting.data.gateway

import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.data.remote.ApiService
import com.melnykov.mvvmtesting.data.remote.request.LoginRequest
import com.melnykov.mvvmtesting.data.remote.response.LoginResponse
import com.melnykov.mvvmtesting.testutil.InstantTaskExecutorRule
import com.melnykov.mvvmtesting.testutil.any
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
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
    private lateinit var call: Call<LoginResponse>

    @Mock
    private lateinit var loginCallbacks: LoginGateway.LoginCallbacks

    @InjectMocks
    private lateinit var loginGateway: LoginGatewayImpl

    private val user = User("makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")

    @Test
    fun login_ExecutesLoginApiCall() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(
                LoginResponse("access_token", user)))

        loginGateway.login("username", "password", loginCallbacks)

        verify(apiService).login(LoginRequest("username", "password"))
        verify(call).execute()

        verifyNoMoreInteractions(apiService)
        verifyNoMoreInteractions(call)
    }

    @Test
    fun loginSuccess_CallsOnLoginSuccessCallback() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(LoginResponse(
                "access_token", user)))

        loginGateway.login("username", "password", loginCallbacks)

        verify(loginCallbacks).onLoginSuccess()
        verifyNoMoreInteractions(loginCallbacks)
    }

    @Test
    fun loginSuccess_InsertsUserIntoDatabase() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(LoginResponse(
                "access_token", user)))

        loginGateway.login("username", "password", loginCallbacks)

        verify(userDao).insert(user)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun nullResponseBody_CallsOnLoginErrorCallback() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.success(null))

        loginGateway.login("username", "password", loginCallbacks)

        verify(loginCallbacks).onLoginError()
        verifyNoMoreInteractions(loginCallbacks)
    }

    @Test
    fun loginInvalidCredentials_CallsOnLoginErrorCallback() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(Response.error(401, ResponseBody.create(
                MediaType.parse("application/json"), "Invalid credentials")))

        loginGateway.login("username", "password", loginCallbacks)

        verify(loginCallbacks).onLoginError()
        verifyNoMoreInteractions(loginCallbacks)
    }

    @Test
    fun serverConnectionProblem_CallsOnLoginErrorCallback() {
        `when`(apiService.login(any())).thenReturn(call)
        `when`(call.execute()).thenThrow(ConnectException("Failed to connect"))

        loginGateway.login("username", "password", loginCallbacks)

        verify(loginCallbacks).onLoginError()
        verifyNoMoreInteractions(loginCallbacks)
    }
}
