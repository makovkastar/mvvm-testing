package com.melnykov.mvvmtesting.testutil

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.melnykov.mvvmtesting.data.local.AppDatabase
import org.junit.After
import org.junit.Before

abstract class DatabaseTest {

    protected lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        db.close()
    }
}
