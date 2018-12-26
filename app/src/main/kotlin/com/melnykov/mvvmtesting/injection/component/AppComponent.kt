package com.melnykov.mvvmtesting.injection.component

import android.app.Application
import com.melnykov.mvvmtesting.MvvmTestingApplication
import com.melnykov.mvvmtesting.injection.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
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
