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

inline fun network(crossinline task: () -> Unit) {
    AppTaskExecutor.executeOnNetworkIO(Runnable { task() })
}

inline fun disk(crossinline task: () -> Unit) {
    AppTaskExecutor.executeOnDiskIO(Runnable { task() })
}

inline fun ui(crossinline task: () -> Unit) {
    AppTaskExecutor.executeOnMainThread(Runnable { task() })
}
