package com.melnykov.mvvmtesting.ui.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.testutil.any
import com.melnykov.mvvmtesting.testutil.capture
import com.melnykov.mvvmtesting.testutil.eq
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var loginGateway: LoginGateway

    @Mock
    private lateinit var navigateToForgotPasswordPageObserver: Observer<Unit>

    @Mock
    private lateinit var navigateToNextScreenObserver: Observer<Unit>

    @Mock
    private lateinit var showErrorToastObserver: Observer<Int>

    @Captor
    private lateinit var loginCallbacks: ArgumentCaptor<LoginGateway.LoginCallbacks>

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
    fun clickLogInButton_CallsLoginGateway() {
        fillLoginCredentials()

        viewModel.onLoginButtonClicked()

        verify(loginGateway).login(eq("username"), eq("password"), any())
    }

    @Test
    fun clickLogInButton_HidesProgressBarOnLoginError() {
        fillLoginCredentials()

        viewModel.onLoginButtonClicked()

        verify(loginGateway).login(any(), any(), capture(loginCallbacks))
        loginCallbacks.value.onLoginError()

        assertThat(viewModel.isProgressBarVisible.get(), `is`(false))
    }

    @Test
    fun clickLoginButton_NavigatesToNextScreenOnLoginSuccess() {
        fillLoginCredentials()

        viewModel.navigateToNextScreen.observeForever(navigateToNextScreenObserver)

        viewModel.onLoginButtonClicked()

        verify(loginGateway).login(any(), any(), capture(loginCallbacks))
        loginCallbacks.value.onLoginSuccess()

        verify(navigateToNextScreenObserver).onChanged(null)
        verifyNoMoreInteractions(navigateToNextScreenObserver)
    }

    @Test
    fun clickLoginButton_ShowsErrorToastOnLoginError() {
        fillLoginCredentials()

        viewModel.showErrorToast.observeForever(showErrorToastObserver)

        viewModel.onLoginButtonClicked()

        verify(loginGateway).login(any(), any(), capture(loginCallbacks))
        loginCallbacks.value.onLoginError()

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
