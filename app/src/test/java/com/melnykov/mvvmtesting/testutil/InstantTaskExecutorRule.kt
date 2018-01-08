package com.melnykov.mvvmtesting.testutil

import com.melnykov.mvvmtesting.data.executor.AppTaskExecutor
import com.melnykov.mvvmtesting.data.executor.TaskExecutor
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class InstantTaskExecutorRule : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        AppTaskExecutor.setDelegate(object : TaskExecutor {
            override fun executeOnMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun executeOnNetworkIO(runnable: Runnable) {
                runnable.run()
            }

            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    override fun finished(description: Description?) {
        super.finished(description)
        AppTaskExecutor.setDelegate(null)
    }
}
