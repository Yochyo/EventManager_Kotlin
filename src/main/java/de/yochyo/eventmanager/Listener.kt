package de.yochyo.eventmanager

interface Listener<T: Event> {
    fun onEvent(e: T)
}