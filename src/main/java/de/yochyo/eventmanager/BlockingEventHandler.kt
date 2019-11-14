package de.yochyo.eventmanager

open class BlockingEventHandler<E : Event> : EventHandler<E>() {
    protected val lock = Any()
    override fun registerListener(l: Listener<E>) = synchronized(lock) { super.registerListener(l) }
    override fun removeAllListeners() = synchronized(lock) { super.removeAllListeners() }
    override fun removeListener(l: Listener<E>) = synchronized(lock) { super.removeListener(l) }
    override fun trigger(e: E) = synchronized(lock) { super.trigger(e) }
}