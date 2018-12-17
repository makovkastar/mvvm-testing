package com.melnykov.mvvmtesting.injection.module

import com.melnykov.mvvmtesting.ui.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {
    @ContributesAndroidInjector
    fun loginFragment(): LoginFragment
}
