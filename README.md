# EventManager_Kotlin

This is a simple Eventmanager written in Kotlin. It does exactly 2 things, manage events and manage listeners you can create without much effort.

Inherit the `Event` class to create an event and add this companion object:
```companion object: EventHandler<YourEvent>()```

Inherit the `Cancelable` class to make your event cancelable.

## Sample code:
```
fun main(args: Array<String>){
    SampleEvent.registerListener(SampleListener)
    SampleEvent.registerListener { println("worked") }

    SampleEvent.trigger(SampleEvent())
}


class SampleEvent: Event, Cancelable{
    override var isCanceled = false

    companion object: EventHandler<SampleEvent>()
}
object SampleListener: Listener<SampleEvent>{
    override fun onEvent(e: SampleEvent) {
        println("worked")
    }
}
```
