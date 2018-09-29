package de.yochyo.eventmanager

import kotlin.collections.ArrayList

abstract class EventHandler<E: Event>{
    val listeners = ArrayList<Listener<E>>()
    fun registerListener(l: Listener<E>) = listeners.add(l)
    fun registerListener(l: (e: E)->Unit): Listener<E>{
        val listener = object : Listener<E> {
            override fun onEvent(e: E) {
                l(e)
            }
        }
        return listener
    }
    fun removeListener(l: Listener<E>) = listeners.remove(l)
    fun removeAllListeners() = listeners.clear()

    fun trigger(e: E) {
        for (l in listeners) {
            l.onEvent(e)
            if (this is Cancelable && this.isCanceled)
                return
        }
    }
}

interface Event {
    val name: String
        get() = this::class.java.simpleName!!
}