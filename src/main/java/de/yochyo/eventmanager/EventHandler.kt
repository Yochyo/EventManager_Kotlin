package de.yochyo.eventmanager

import java.util.*
import java.util.concurrent.LinkedBlockingQueue

open class EventHandler<E : Event>: MutableList<Listener<E>> by LinkedList(){
    var locked = false
    private val doAfterUnlock = LinkedBlockingQueue<() -> Unit>()

    /**
     * Adds a listener to the evenhandler. This method has to be used if a listener should be added while an event is being triggered.
     * This can happen if a listener registers another listener. If an event is being triggered right now, the listener will be added
     * after the event was proceeded.
     * Do not use add() if you want to add a listener while an event is being triggered
     */
    open fun registerListener(l: Listener<E>) {
        synchronized(locked) {
            if (!locked) add(l)
            else doAfterUnlock.add { add(l) }
        }
    }

    /**
     * Removes a listener from the evenhandler. This method has to be used if a listener should be removed while an event is being triggered.
     * This can happen if a listener removes another listener. If an event is being triggered right now, the listener will be removed
     * after the event was proceeded.
     * Do not use remove() if you want to remove a listener while an event is being triggered
     */
    open fun removeListener(l: Listener<E>){
        synchronized(locked) {
            if (!locked) remove(l)
            else doAfterUnlock.add { remove(l) }
        }
    }

    /**
     * Removes all listeners from the evenhandler. This method has to be used if the listeners should be removed while an event is being triggered.
     * This can happen if a listener calls this function. If an event is being triggered right now, the listeners will be removed
     * after the event was proceeded.
     * Do not use removeAll() if you want to remove all listeners while an event is being triggered
     */
    open fun removeAllListeners() {
        synchronized(locked) {
            if (!locked) clear()
            else doAfterUnlock.add { clear() }
        }
    }


    open fun trigger(e: E) {
        synchronized(locked){
            if(!locked){
                locked = true

                val iter = iterator()
                while (iter.hasNext()) {
                    iter.next().onEvent(e)
                    if (e.deleteListener) {
                        e.deleteListener = false
                        iter.remove()
                    }
                }

                while(doAfterUnlock.isNotEmpty())
                    doAfterUnlock.take()()
                locked = false
            } else doAfterUnlock.add { trigger(e) }
        }
    }
}