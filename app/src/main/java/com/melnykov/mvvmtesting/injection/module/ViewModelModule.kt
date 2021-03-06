package com.melnykov.mvvmtesting.injection.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.melnykov.mvvmtesting.injection.ViewModelFactory
import com.melnykov.mvvmtesting.injection.ViewModelKey
import com.melnykov.mvvmtesting.ui.login.LoginViewModel

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}