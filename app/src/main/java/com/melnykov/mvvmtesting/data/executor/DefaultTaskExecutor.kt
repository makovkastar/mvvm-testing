package com.melnykov.mvvmtesting.data.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DefaultTaskExecutor : TaskExecutor {

    private val diskIO: Executor = Executors.newSingleThreadExecutor()
    private val networkIO: Executor = Executors.newFixedThreadPool(3)
    private val mainThread: Executor = MainThreadExecutor()

    override fun executeOnDiskIO(runnable: Runnable) {
        diskIO.execute(runnable)
    }

    override fun executeOnNetworkIO(runnable: Runnable) {
        networkIO.execute(runnable)
    }

    override fun executeOnMainThread(runnable: Runnable) {
        mainThread.execute(runnable)
    }

    private class MainThreadExecutor : Executor {

        private val mainThreadHandler = lazy {
            Handler(Looper.getMainLooper())
        }

        override fun execute(command: Runnable) {
            mainThreadHandler.value.post(command)
        }
    }
}
