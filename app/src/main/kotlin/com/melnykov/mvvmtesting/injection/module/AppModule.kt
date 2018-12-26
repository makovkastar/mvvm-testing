package com.melnykov.mvvmtesting.injection.module

import com.melnykov.mvvmtesting.injection.qualifier.BgContext
import com.melnykov.mvvmtesting.injection.qualifier.UiContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
object AppModule {
    @Provides
    @UiContext
    @JvmStatic
    fun provideCoroutineUiContext(): CoroutineContext = Dispatchers.Main

    @Provides
    @BgContext
    @JvmStatic
    fun provideCoroutineBgContext(): CoroutineContext = Dispatchers.IO
}
