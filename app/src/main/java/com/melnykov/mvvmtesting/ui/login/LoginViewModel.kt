package com.melnykov.mvvmtesting.ui.login

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import com.melnykov.mvvmtesting.OpenForTesting
import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.util.SingleLiveEvent
import com.melnykov.mvvmtesting.util.TextWatcherAdapter
import javax.inject.Inject

@OpenForTesting
class LoginViewModel @Inject constructor(private val loginGateway: LoginGateway) : ViewModel() {

    val usernameField = ObservableField<String>()
    val passwordField = ObservableField<String>()

    val isLogInButtonEnabled = ObservableBoolean(false)

    val isProgressBarVisible = ObservableBoolean(false)

    val navigateToForgotPasswordPage = SingleLiveEvent<Unit>()

    val navigateToNextScreen = SingleLiveEvent<Unit>()

    val showErrorToast = SingleLiveEvent<Int>()

    fun onLoginButtonClicked() {
        isProgressBarVisible.set(true)
        loginGateway.login(
            checkNotNull(usernameField.get()),
            checkNotNull(passwordField.get()),
            LoginCallbacks()
        )
    }

    fun onForgotPasswordLabelClicked() {
        navigateToForgotPasswordPage.call()
    }

    fun usernameTextChangedListener(): TextWatcher {
        return object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                validateInputFields()
            }
        }
    }

    fun passwordTextChangedListener(): TextWatcher {
        return object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                validateInputFields()
            }
        }
    }

    private fun validateInputFields() {
        isLogInButtonEnabled.set(!usernameField.get().isNullOrBlank()
            && !passwordField.get().isNullOrBlank())
    }

    inner class LoginCallbacks : LoginGateway.LoginCallbacks {

        override fun onLoginSuccess() {
            navigateToNextScreen.call()
        }

        override fun onLoginError() {
            isProgressBarVisible.set(false)
            showErrorToast.call()
        }
    }
}
