package de.yochyo.eventmanager

import java.util.*

open class EventHandler<E: Event>{
    private val lock = Any()
    private val listeners = LinkedList<Listener<E>>()
    fun registerListener(l: Listener<E>){
        synchronized(lock){
            val res = listeners.add(l)
            listeners.sortBy { it }
        }
    }
    fun registerListener(l: (e: E)->Unit): Listener<E>{
        val listener = object : Listener<E>() {
            override fun onEvent(e: E) = l(e)
        }
        registerListener(listener)
        return listener
    }

    fun removeListener(l: Listener<E>) = synchronized(lock){listeners.remove(l)}
    fun removeAllListeners() = synchronized(lock){listeners.clear()}

    open fun trigger(e: E) {
        synchronized(lock){
            val iter = listeners.iterator()
            while(iter.hasNext() && !e.isCanceled){
                iter.next().onEvent(e)
                if(e.deleteListener){
                    e.deleteListener = false
                    iter.remove()
                }
            }
        }
    }
}

abstract class Event {
    open val name: String
        get() = this::class.java.simpleName!!
    var isCanceled = false
    var deleteListener = false
}