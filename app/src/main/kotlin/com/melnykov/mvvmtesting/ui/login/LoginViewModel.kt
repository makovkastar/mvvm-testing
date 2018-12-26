package com.melnykov.mvvmtesting.ui.login

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import com.melnykov.mvvmtesting.OpenForTesting
import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.data.gateway.LoginResult
import com.melnykov.mvvmtesting.injection.qualifier.UiContext
import com.melnykov.mvvmtesting.util.SingleLiveEvent
import com.melnykov.mvvmtesting.util.TextWatcherAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@OpenForTesting
class LoginViewModel @Inject constructor(
    private val loginGateway: LoginGateway,
    @UiContext private val uiContext: CoroutineContext
) : ViewModel() {

    val usernameField = ObservableField<String>()
    val passwordField = ObservableField<String>()

    val isLogInButtonEnabled = ObservableBoolean(false)

    val isProgressBarVisible = ObservableBoolean(false)

    val navigateToForgotPasswordPage = SingleLiveEvent<Unit>()

    val navigateToNextScreen = SingleLiveEvent<Unit>()

    val showErrorToast = SingleLiveEvent<Int>()

    fun onLoginButtonClicked() = GlobalScope.launch(uiContext) {
        isProgressBarVisible.set(true)

        val result = loginGateway.login(
            checkNotNull(usernameField.get()),
            checkNotNull(passwordField.get())
        )

        when (result) {
            is LoginResult.Success -> {
                navigateToNextScreen.call()
            }
            is LoginResult.Error -> {
                isProgressBarVisible.set(false)
                showErrorToast.call()
            }
        }
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
        isLogInButtonEnabled.set(!usernameField.get().isNullOrBlank() &&
            !passwordField.get().isNullOrBlank())
    }
}
