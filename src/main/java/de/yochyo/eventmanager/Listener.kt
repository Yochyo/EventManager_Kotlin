package de.yochyo.eventmanager

fun interface Listener<T : Event> {
    companion object {
        fun <E : Event> create(l: (e: E) -> Unit): Listener<E> {
            val listener = Listener<E> { l(it) }
            return listener
        }
    }

    fun onEvent(e: T)
}