package de.yochyo.eventmanager.suspended

import de.yochyo.eventmanager.Event

abstract class SuspendedListener<T : Event>(val priority: Int = NORMAL) : Comparable<SuspendedListener<T>> {
    companion object {
        const val HIGHEST = 0
        const val HIGHER = 1
        const val HIGH = 2
        const val NORMAL = 3
        const val LOW = 4
        const val LOWER = 5
        const val LOWEST = 6

        fun <E : Event> create(priority: Int = NORMAL, l: suspend (e: E) -> Unit): SuspendedListener<E> {
            val listener = object : SuspendedListener<E>(priority) {
                override suspend fun onEvent(e: E) = l(e)
            }
            return listener
        }
    }

    abstract suspend fun onEvent(e: T)

    override fun compareTo(other: SuspendedListener<T>) = priority.compareTo(other.priority)
}