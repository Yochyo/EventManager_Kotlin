package de.yochyo.eventmanager

import java.util.function.Predicate


open class EventCollection<T>(private val c: MutableCollection<T>) : MutableCollection<T> {
    val onUpdate = object : EventHandler<OnUpdateEvent>() {}
    val onClear = object : EventHandler<OnClearEvent>() {
        override fun trigger(e: EventCollection<T>.OnClearEvent) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onAddElement = object : EventHandler<OnAddElementEvent>() {
        override fun trigger(e: EventCollection<T>.OnAddElementEvent) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onAddElements = object : EventHandler<OnAddElementsEvent>() {
        override fun trigger(e: EventCollection<T>.OnAddElementsEvent) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onRemoveElement = object : EventHandler<OnRemoveElementEvent>() {
        override fun trigger(e: EventCollection<T>.OnRemoveElementEvent) {
            super.trigger(e)
            notifyChange()
        }
    }

    init {
        notifyChange()
    }

    fun notifyChange() {
        onUpdate.trigger(OnUpdateEvent(c))
    }

    override val size: Int get() = c.size
    override fun contains(element: T) = c.contains(element)
    override fun containsAll(elements: Collection<T>) = c.containsAll(elements)
    override fun isEmpty() = c.isEmpty()
    operator fun get(index: Int) = c.elementAt(index)

    override fun add(e: T): Boolean {
        val res = c.add(e)
        if (res)
            onAddElement.trigger(OnAddElementEvent(c, e))
        return res
    }

    override fun addAll(e: Collection<T>): Boolean {
        val res = c.addAll(e)
        if (res)
            onAddElements.trigger(OnAddElementsEvent(c, e))
        return res
    }

    override fun remove(e: T): Boolean {
        val res = c.remove(e)
        if (res)
            onRemoveElement.trigger(OnRemoveElementEvent(c, e))
        return res
    }

    override fun clear() {
        c.clear()
        onClear.trigger(OnClearEvent(c))
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var removed = false
        for (e in elements)
            if (remove(e)) removed = true
        return removed
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        var removed = false
        val iter = c.iterator()
        while (iter.hasNext()) {
            val current = iter.next()
            if (filter.test(current)) {
                iter.remove()
                removed = true
                onRemoveElement.trigger(OnRemoveElementEvent(c, current))
            }
        }
        return removed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val iter = c.iterator()
        var removed = false
        while(iter.hasNext()){
            val current = iter.next()
            if(!elements.contains(current)){
                iter.remove()
                onRemoveElement.trigger(OnRemoveElementEvent(c, current))
                removed = true
            }
        }
        return removed
    }

    override fun iterator() = c.iterator()
    override fun parallelStream() = c.parallelStream()

    override fun spliterator() = c.spliterator()

    inner class OnUpdateEvent(val collection: Collection<T>) : Event()
    inner class OnClearEvent(val collection: Collection<T>) : Event()
    inner class OnAddElementEvent(val collection: Collection<T>, val element: T) : Event()
    inner class OnAddElementsEvent(val collection: Collection<T>, val elements: Collection<T>) : Event()
    inner class OnRemoveElementEvent(val collection: Collection<T>, val element: T) : Event()
}
