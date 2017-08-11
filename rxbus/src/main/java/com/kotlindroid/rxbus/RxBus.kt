package com.kotlindroid.rxbus

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.util.ArrayMap
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * Created by arnis on 22.05.17.
 */

object RxBus {
    internal const val MAIN = "main"
    private lateinit var channels: ArrayMap<String, RxChannel>

    @JvmStatic
    @JvmOverloads
    fun init(names: List<String> = listOf(MAIN)) {
        channels = ArrayMap()
        names.forEach { channels.put(it, RxChannel(it)) }
    }

    @JvmStatic
    @JvmOverloads
    fun send(channel: String = MAIN, event: RxBusEvent) = channels[channel]?.let {
        it.send(event)
        true
    } ?: false

    @JvmStatic
    @JvmOverloads
    fun sendFor(channel: String = MAIN, subscriber: Any, event: RxBusEvent) = channels[channel]?.let {
        val sub = subscriber::class.java.simpleName
        if (it.subscriptions.containsKey(sub)) {
            send(channel, event)
            true
        } else {
            it.pendingEvents.put(sub, event)
            false
        }
    } ?: false

    @JvmStatic
    @JvmOverloads
    fun subscribe(channel: String = MAIN, subscriber: Any, observer: Consumer<RxBusEvent>) = channels[channel]?.let {
        val sub = subscriber::class.java.simpleName

        if (it.subscriptions.containsKey(sub))
            it.remove(sub)

        it.subscriptions.put(sub, it.pipe.subscribe(observer, Consumer { it.printStackTrace() }))
        true
    } ?: false

    @JvmStatic
    @JvmOverloads
    fun unsubscribe(channel: String = MAIN, subscriber: Any) = channels[channel]?.let {
        it.remove(subscriber::class.java.simpleName)
        true
    } ?: false

    @JvmStatic
    @JvmOverloads
    fun hasSubscriber(channel: String = MAIN, subscriber: Any) = channels[channel]
            ?.subscriptions?.containsKey(subscriber::class.java.simpleName) ?: false

    @JvmStatic
    @JvmOverloads
    fun requestPendingEvents(channel: String = MAIN, subscriber: Any) {
        channels[channel]?.apply {
            val sub = subscriber::class.java.simpleName
            if (pendingEvents.containsKey(sub) && hasSubscriber(subscriber = sub)) {
                pendingEvents[sub]?.let { send(it) }
                pendingEvents.remove(sub)
            }
        }
    }
}

//interface RxBusSubscriptionManager: LifecycleObserver {
//
//    fun getChannel() = RxBus.MAIN
//
//    fun subscribeTo(lifecycle: Lifecycle) = lifecycle.addObserver(this)
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
//    fun unsubscribe() {
//        RxBus.unsubscribe(getChannel(), this::class.java.simpleName)
//    }
//}

data class RxChannel(val name: String) {
    internal val subscriptions = ArrayMap<String, Disposable>()
    internal val pendingEvents = ArrayMap<String, RxBusEvent>()
    internal val pipe = PublishSubject.create<RxBusEvent>()

    internal fun send(event: RxBusEvent) = pipe.onNext(event)

    internal fun remove(id: String) {
        subscriptions[id]?.dispose()
        subscriptions.remove(id)
    }
}

interface RxBusEvent {
    val content: Any
}

