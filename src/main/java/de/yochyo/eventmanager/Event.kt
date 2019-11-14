package de.yochyo.eventmanager

import java.util.*

abstract class Event {
    open val name: String
        get() = this::class.java.simpleName!!
    var deleteListener = false
}