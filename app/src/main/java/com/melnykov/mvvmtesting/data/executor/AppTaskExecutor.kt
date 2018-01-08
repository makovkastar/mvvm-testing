package com.melnykov.mvvmtesting.data.executor

object AppTaskExecutor : TaskExecutor {

    private val defaultTaskExecutor: TaskExecutor

    private var delegate: TaskExecutor

    init {
        defaultTaskExecutor = DefaultTaskExecutor()
        delegate = defaultTaskExecutor
    }

    fun setDelegate(taskExecutor: TaskExecutor?) {
        delegate = taskExecutor ?: defaultTaskExecutor
    }

    override fun executeOnDiskIO(runnable: Runnable) {
        delegate.executeOnDiskIO(runnable)
    }

    override fun executeOnNetworkIO(runnable: Runnable) {
        delegate.executeOnNetworkIO(runnable)
    }

    override fun executeOnMainThread(runnable: Runnable) {
        delegate.executeOnMainThread(runnable)
    }
}

fun network(task: () -> Unit) {
    AppTaskExecutor.executeOnNetworkIO(Runnable { task() })
}

fun disk(task: () -> Unit) {
    AppTaskExecutor.executeOnDiskIO(Runnable { task() })
}

fun ui(task: () -> Unit) {
    AppTaskExecutor.executeOnMainThread(Runnable { task() })
}
