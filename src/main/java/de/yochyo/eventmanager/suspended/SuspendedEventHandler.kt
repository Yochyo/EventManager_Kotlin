package de.yochyo.eventmanager.suspended

import de.yochyo.eventmanager.Event
import de.yochyo.eventmanager.Listener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.*

open class SuspendedEventHandler<E : Event>(val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    protected val mutex = Mutex()
    private val listeners = LinkedList<SuspendedListener<E>>()
    open suspend fun registerListener(l: SuspendedListener<E>) {
        mutex.withLock {
            listeners.add(l)
            listeners.sortBy { it }
        }
    }

    suspend fun registerListener(priority: Int = Listener.NORMAL, l: suspend (e: E) -> Unit): SuspendedListener<E> {
        val listener = object : SuspendedListener<E>() {
            override suspend fun onEvent(e: E) = l(e)
        }
        registerListener(listener)
        return listener
    }

    open suspend fun removeListener(l: SuspendedListener<E>) = mutex.withLock { listeners.remove(l) }
    open suspend fun removeAllListeners() = mutex.withLock { listeners.clear() }

    open suspend fun trigger(e: E, dispatcher: CoroutineDispatcher = defaultDispatcher) {
        withContext(dispatcher) {
            mutex.withLock {
                val iter = listeners.iterator()
                while (iter.hasNext()) {
                    iter.next().onEvent(e)
                    if (e.deleteListener) {
                        e.deleteListener = false
                        iter.remove()
                    }
                }
            }
        }
    }
}