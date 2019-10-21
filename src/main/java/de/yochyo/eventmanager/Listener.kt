package de.yochyo.eventmanager

abstract class Listener<T: Event>(val priority: Int = NORMAL): Comparable<Listener<T>> {
    companion object{
        const val HIGHEST = 0
        const val HIGHER = 1
        const val HIGH = 2
        const val NORMAL = 3
        const val LOW = 4
        const val LOWER = 5
        const val LOWEST = 6

        fun <E: Event> create(priority: Int = NORMAL, l: (e: E)->Unit): Listener<E>{
            val listener = object : Listener<E>(priority) {
                override fun onEvent(e: E) = l(e)
            }
            return listener
        }
    }

    override fun compareTo(other: Listener<T>) = priority.compareTo(other.priority)

    abstract fun onEvent(e: T)
}