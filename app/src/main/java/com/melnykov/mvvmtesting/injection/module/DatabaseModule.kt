package com.melnykov.mvvmtesting.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import com.melnykov.mvvmtesting.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
                application, AppDatabase::class.java, "mvvm-testing.db").build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
}
