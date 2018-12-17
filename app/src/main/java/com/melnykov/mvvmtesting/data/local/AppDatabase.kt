package com.melnykov.mvvmtesting.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.melnykov.mvvmtesting.data.local.dao.UserDao
import com.melnykov.mvvmtesting.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
