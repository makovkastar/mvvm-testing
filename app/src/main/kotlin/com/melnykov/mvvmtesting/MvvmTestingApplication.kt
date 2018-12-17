package com.melnykov.mvvmtesting

import com.melnykov.mvvmtesting.injection.Injector
import com.melnykov.mvvmtesting.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MvvmTestingApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
