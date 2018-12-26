package com.melnykov.mvvmtesting.ui.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.data.gateway.LoginResult
import com.melnykov.mvvmtesting.testutil.any
import com.melnykov.mvvmtesting.testutil.eq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginGateway: LoginGateway

    @Mock
    private lateinit var navigateToForgotPasswordPageObserver: Observer<Unit>

    @Mock
    private lateinit var navigateToNextScreenObserver: Observer<Unit>

    @Mock
    private lateinit var showErrorToastObserver: Observer<Int>

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(loginGateway, Dispatchers.Unconfined)
    }

    @Test
    fun logInButtonIsDisabledInitially() {
        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(false))
    }

    @Test
    fun logInButtonIsDisabled_IfPasswordIsEmpty() {
        viewModel.usernameField.set("username")
        viewModel.usernameTextChangedListener().afterTextChanged(null)

        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(false))
    }

    @Test
    fun logInButtonIsDisabled_IfUsernameIsEmpty() {
        viewModel.passwordField.set("password")
        viewModel.passwordTextChangedListener().afterTextChanged(null)

        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(false))
    }

    @Test
    fun logInButtonIsDisabled_IfPasswordIsBlank() {
        viewModel.usernameField.set("username")
        viewModel.usernameTextChangedListener().afterTextChanged(null)

        viewModel.passwordField.set("        ")
        viewModel.passwordTextChangedListener().afterTextChanged(null)

        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(false))
    }

    @Test
    fun logInButtonIsDisabled_IfUsernameIsBlank() {
        viewModel.usernameField.set("     ")
        viewModel.usernameTextChangedListener().afterTextChanged(null)

        viewModel.passwordField.set("password")
        viewModel.passwordTextChangedListener().afterTextChanged(null)

        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(false))
    }

    @Test
    fun logInButtonIsEnabled_IfUsernameAndPasswordAreNotEmpty() {
        viewModel.usernameField.set("username")
        viewModel.usernameTextChangedListener().afterTextChanged(null)

        viewModel.passwordField.set("password")
        viewModel.passwordTextChangedListener().afterTextChanged(null)

        assertThat(viewModel.isLogInButtonEnabled.get(), `is`(true))
    }

    @Test
    fun progressBarIsInvisibleInitially() {
        assertThat(viewModel.isProgressBarVisible.get(), `is`(false))
    }

    @Test
    fun clickLoginButton_ShowsProgressBar() {
        fillLoginCredentials()

        viewModel.onLoginButtonClicked()

        assertThat(viewModel.isProgressBarVisible.get(), `is`(true))
    }

    @Test
    fun clickLogInButton_CallsLoginGateway() = runBlocking<Unit> {
        fillLoginCredentials()

        viewModel.onLoginButtonClicked()

        verify(loginGateway).login(eq("username"), eq("password"))
    }

    @Test
    fun clickLogInButton_HidesProgressBarOnLoginError() = runBlocking {
        fillLoginCredentials()

        `when`(loginGateway.login(any(), any()))
            .thenReturn(LoginResult.Error)

        viewModel.onLoginButtonClicked()

        assertThat(viewModel.isProgressBarVisible.get(), `is`(false))
    }

    @Test
    fun clickLoginButton_NavigatesToNextScreenOnLoginSuccess() = runBlocking {
        fillLoginCredentials()

        viewModel.navigateToNextScreen.observeForever(navigateToNextScreenObserver)

        `when`(loginGateway.login(any(), any()))
            .thenReturn(LoginResult.Success)

        viewModel.onLoginButtonClicked()

        verify(navigateToNextScreenObserver).onChanged(null)
        verifyNoMoreInteractions(navigateToNextScreenObserver)
    }

    @Test
    fun clickLoginButton_ShowsErrorToastOnLoginError() = runBlocking {
        fillLoginCredentials()

        viewModel.showErrorToast.observeForever(showErrorToastObserver)

        `when`(loginGateway.login(any(), any()))
            .thenReturn(LoginResult.Error)

        viewModel.onLoginButtonClicked()

        verify(showErrorToastObserver).onChanged(null)
        verifyNoMoreInteractions(showErrorToastObserver)
    }

    @Test
    fun clickForgotPasswordLabel_NavigatesToForgotPasswordPage() {
        viewModel.navigateToForgotPasswordPage.observeForever(
            navigateToForgotPasswordPageObserver)

        viewModel.onForgotPasswordLabelClicked()

        verify(navigateToForgotPasswordPageObserver).onChanged(null)
        verifyNoMoreInteractions(navigateToForgotPasswordPageObserver)
    }

    private fun fillLoginCredentials() {
        viewModel.usernameField.set("username")
        viewModel.passwordField.set("password")
    }
}
