package de.yochyo.eventmanager

import java.util.*

abstract class EventHandler<E: Event>{
    private val listeners = LinkedList<Listener<E>>()
    fun registerListener(l: Listener<E>) = listeners.add(l)
    fun registerListener(l: (e: E)->Boolean): Listener<E>{
        val listener = object : Listener<E> {
            override fun onEvent(e: E): Boolean {
                return l(e)
            }
        }
        registerListener(listener)
        return listener
    }
    fun registerSingleUseListener(l: (e: E)->Boolean): Listener<E>{
        val listener = object : SingleUseListener<E> {
            override fun onEvent(e: E): Boolean {
                return l(e)
            }
        }
        registerListener(listener)
        return listener
    }
    fun removeListener(l: Listener<E>) = listeners.remove(l)
    fun removeAllListeners() = listeners.clear()

    fun trigger(e: E) {
        val iter = listeners.iterator()
        while(iter.hasNext()){
            val next = iter.next()
            val result = next.onEvent(e)
            if(next is SingleUseListener && result)
                iter.remove()
        }
    }
}

interface Event {
    val name: String
        get() = this::class.java.simpleName!!
}