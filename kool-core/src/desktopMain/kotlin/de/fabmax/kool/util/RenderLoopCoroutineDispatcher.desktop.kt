package de.fabmax.kool.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class KoolMainDispatcher : CoroutineDispatcher() {
    private val pool = Executors.newSingleThreadExecutor()
    private val dispatcher = pool.asCoroutineDispatcher()
    private var mainThread: Thread? = null

    init {
        pool.execute { mainThread = Thread.currentThread() }
    }

    //    private val singleThread = newSingleThreadContext("kool-ui-thread")
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
//        println("isDispatchNeeded: ${Thread.currentThread().name} / ${mainThread?.name}")
        return Thread.currentThread() != mainThread
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatcher.dispatch(context, block)
    }

    fun close() {
        dispatcher.close()
    }
}

val uiDispatcher: KoolMainDispatcher = KoolMainDispatcher()

actual val Dispatchers.MainUI: CoroutineDispatcher
    get() = uiDispatcher
