package de.yochyo.eventmanager

import java.util.function.Predicate


class EventCollection<T>(private val c: MutableCollection<T>, val onUpdate: (collection: Collection<T>) -> Unit = {}) : MutableCollection<T> {

    init {
        notifyChange()
    }

    override val size: Int get() = c.size
    override fun contains(element: T) = c.contains(element)
    override fun containsAll(elements: Collection<T>) = c.containsAll(elements)
    override fun isEmpty() = c.isEmpty()
    fun isNotEmpty() = c.isNotEmpty()
    operator fun get(index: Int) = c.elementAt(index)

    override fun add(e: T): Boolean {
        val res = c.add(e)
        if (res)
            notifyChange()
        return res
    }

    override fun addAll(e: Collection<T>): Boolean {
        val res = c.addAll(e)
        if (res)
            notifyChange()
        return res
    }

    override fun remove(e: T): Boolean {
        val res = c.remove(e)
        if (res)
            notifyChange()
        return res
    }

    override fun clear() {
        c.clear()
        notifyChange()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val res = c.removeAll(elements)
        if (res)
            notifyChange()
        return res
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        val res = c.removeIf(filter)
        if (res)
            notifyChange()
        return res
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val res = c.retainAll(elements)
        if (res)
            notifyChange()
        return res
    }

    override fun iterator() = c.iterator()
    override fun parallelStream() = c.parallelStream()

    override fun spliterator() = c.spliterator()


    fun notifyChange() {
        onUpdate(c)
    }

}