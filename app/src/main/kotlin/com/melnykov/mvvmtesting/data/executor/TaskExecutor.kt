package com.melnykov.mvvmtesting.data.executor

interface TaskExecutor {
    fun executeOnDiskIO(runnable: Runnable)
    fun executeOnNetworkIO(runnable: Runnable)
    fun executeOnMainThread(runnable: Runnable)
}
