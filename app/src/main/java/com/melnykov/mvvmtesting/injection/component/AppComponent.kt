package com.melnykov.mvvmtesting.injection.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import com.melnykov.mvvmtesting.MvvmTestingApplication
import com.melnykov.mvvmtesting.injection.module.FragmentModule
import com.melnykov.mvvmtesting.injection.module.GatewayModule
import com.melnykov.mvvmtesting.injection.module.NetworkModule
import com.melnykov.mvvmtesting.injection.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    FragmentModule::class,
    ViewModelModule::class,
    GatewayModule::class,
    NetworkModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<MvvmTestingApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}
