package de.yochyo.eventmanager

import java.util.*

open class EventHandler<E : Event> {
    private val listeners = LinkedList<Listener<E>>()

    open fun registerListener(l: Listener<E>) {
        listeners.add(l)
    }

    open fun registerListener(l: (e: E) -> Unit): Listener<E> {
        val listener = Listener<E> { l(it) }
        registerListener(listener)
        return listener
    }

    open fun removeListener(l: Listener<E>) = listeners.remove(l)
    open fun removeAllListeners(): List<Listener<E>> {
        val res = listeners.clone() as List<Listener<E>>
        listeners.clear()
        return res
    }

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