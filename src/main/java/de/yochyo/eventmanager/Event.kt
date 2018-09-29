package de.yochyo.eventmanager

interface Event {
    companion object {
        private val listeners = HashMap<Class<*>, ArrayList<Listener>>()
        fun getListener(event: Class<*>): ArrayList<Listener> = listeners[event]!!
        fun registerListener(event: Class<*>, l: Listener) {
            if (listeners[event] == null) {
                listeners[event] = arrayListOf(l)
                return
            }
            listeners[event]!!.add(l)
        }

        fun unregisterEvent(event: Class<*>, l: Listener) {
            listeners[event]!!.remove(l)
            //TODO concurrentException?
        }
    }

    val name: String
        get() = this::class.java.simpleName!!

    fun trigger() {
        val listeners = getListener(this::class.java)
        for (l in listeners) {
            l.onEvent(this)
            if (this is Cancelable && this.isCanceled)
                return
        }
        execute(this)
    }

    fun execute(e: Event)
}