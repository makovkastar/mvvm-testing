package com.melnykov.mvvmtesting.ui.login

import android.app.Activity
import android.app.Instrumentation
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasData
import android.support.test.espresso.intent.matcher.IntentMatchers.isInternal
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import android.text.TextWatcher
import com.melnykov.mvvmtesting.R
import com.melnykov.mvvmtesting.testutil.SingleFragmentActivity
import com.melnykov.mvvmtesting.testutil.ToastMatchers.isToast
import com.melnykov.mvvmtesting.ui.home.HomeActivity
import com.melnykov.mvvmtesting.util.SingleLiveEvent
import com.melnykov.mvvmtesting.util.Urls
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(SingleFragmentActivity::class.java)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var usernameTextChangedListener: TextWatcher

    @Mock
    private lateinit var passwordTextChangedListener: TextWatcher

    private val usernameField = ObservableField<String>()
    private val passwordField = ObservableField<String>()

    private val isLogInButtonEnabled = ObservableBoolean()
    private val isProgressBarVisible = ObservableBoolean()

    private val navigateToForgotPasswordPage = SingleLiveEvent<Unit>()
    private val navigateToNextScreen = SingleLiveEvent<Unit>()
    private val showErrorToast = SingleLiveEvent<Int>()

    private lateinit var loginFragment: LoginFragment

    @Before
    fun setUp() {
        loginFragment = LoginFragment()

        `when`(viewModel.usernameField).thenReturn(usernameField)
        `when`(viewModel.passwordField).thenReturn(passwordField)

        `when`(viewModel.isLogInButtonEnabled).thenReturn(isLogInButtonEnabled)
        `when`(viewModel.isProgressBarVisible).thenReturn(isProgressBarVisible)

        `when`(viewModel.navigateToForgotPasswordPage).thenReturn(navigateToForgotPasswordPage)
        `when`(viewModel.navigateToNextScreen).thenReturn(navigateToNextScreen)
        `when`(viewModel.showErrorToast).thenReturn(showErrorToast)

        `when`(viewModel.usernameTextChangedListener()).thenReturn(usernameTextChangedListener)
        `when`(viewModel.passwordTextChangedListener()).thenReturn(passwordTextChangedListener)

        loginFragment.viewModelFactory = createViewModelFactory(viewModel)

        activityRule.activity.setFragment(loginFragment)
    }

    @Test
    fun usernameAndPasswordAreEmptyInitially() {
        onView(withId(R.id.edit_text_username)).check(matches(withText("")))
        onView(withId(R.id.edit_text_password)).check(matches(withText("")))
    }

    @Test
    fun clickForgotPasswordLabel_CallsViewModel() {
        onView(withId(R.id.label_forgot_password)).perform(click())
        verify(viewModel).onForgotPasswordLabelClicked()
    }

    @Test
    fun clickLogInButton_CallsViewModel() {
        isLogInButtonEnabled.set(true)

        onView(withId(R.id.button_log_in)).perform(click())

        verify(viewModel).onLoginButtonClicked()
    }

    @Test
    fun navigateToForgotPasswordPage_StartsBrowser() {
        // We assume that an emulator or device has a browser installed.
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        navigateToForgotPasswordPage.call()

        intended(allOf(hasAction(Intent.ACTION_VIEW),
            hasData(Urls.FORGOT_PASSWORD)))
    }

    @Test
    fun navigateToNextScreen_StartsHomeActivity() {
        navigateToNextScreen.call()
        intended(hasComponent(HomeActivity::class.java.name))
    }

    @Test
    @Ignore("NoMatchingViewException is thrown.")
    fun showErrorToast_ShowsToastWithCorrectText() {
        showErrorToast.value = R.string.general_toast_no_application_available
        onView(withText(R.string.general_toast_no_application_available))
            .inRoot(isToast()).check(matches(isDisplayed()))
    }

    @Test
    fun isLogInButtonEnabledBinding() {
        isLogInButtonEnabled.set(true)
        onView(withId(R.id.button_log_in)).check(matches(isEnabled()))
    }

    @Test
    fun isProgressBarVisibleBinding() {
        isProgressBarVisible.set(true)
        onView(withId(R.id.progress_overlay)).check(matches(isDisplayed()))
    }

    @Test
    fun usernameTextChangedListenerBinding() {
        onView(withId(R.id.edit_text_username)).perform(
            typeText("username"), closeSoftKeyboard())

        verify(usernameTextChangedListener, times(8)).afterTextChanged(any())
    }

    @Test
    fun passwordTextChangedListenerBinding() {
        onView(withId(R.id.edit_text_password)).perform(
            typeText("password"), closeSoftKeyboard())

        verify(passwordTextChangedListener, times(8)).afterTextChanged(any())
    }

    @Test
    fun usernameFieldBinding() {
        onView(withId(R.id.edit_text_username)).perform(
            typeText("username"), closeSoftKeyboard())

        assertThat(usernameField.get(), `is`("username"))
    }

    @Test
    fun passwordFieldBinding() {
        onView(withId(R.id.edit_text_password)).perform(
            typeText("password"), closeSoftKeyboard())

        assertThat(passwordField.get(), `is`("password"))
    }

    private fun <T : ViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
                if (viewModelClass.isAssignableFrom(viewModel.javaClass)) {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                }
                throw IllegalArgumentException("Unknown view model class $viewModelClass")
            }
        }
    }

    fun <T> any(): T = Mockito.any<T>()
}
