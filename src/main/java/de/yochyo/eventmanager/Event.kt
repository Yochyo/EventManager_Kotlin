package de.yochyo.eventmanager

private val events = HashMap<Event, Array<out Listener>>()
abstract class Event{
    fun register(vararg listeners: Listener){
        events[this] = listeners
    }
}

fun executeEvent(e: Event){
    val c = e.javaClass
    events.keys.forEach {
        if(it.javaClass == c) {
            events[it]!!.forEach {
                it.onEvent(e)
            }
        }
    }
}
