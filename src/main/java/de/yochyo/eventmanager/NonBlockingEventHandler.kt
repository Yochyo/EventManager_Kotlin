package de.yochyo.eventmanager

import java.util.*

open class NonBlockingEventHandler<E : Event> {
    private val listeners = LinkedList<Listener<E>>()
    open fun registerListener(l: Listener<E>) {
        listeners.add(l)
        listeners.sortBy { it }
    }

    fun registerListener(priority: Int = Listener.NORMAL, l: (e: E) -> Unit): Listener<E> {
        val listener = object : Listener<E>(priority) {
            override fun onEvent(e: E) = l(e)
        }
        registerListener(listener)
        return listener
    }

    open fun removeListener(l: Listener<E>) = listeners.remove(l)
    open fun removeAllListeners() = listeners.clear()

    open fun trigger(e: E) {
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