package com.melnykov.mvvmtesting.testutil

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import com.melnykov.mvvmtesting.AndroidTestApplication

class AndroidTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, AndroidTestApplication::class.java.name, context)
    }
}
