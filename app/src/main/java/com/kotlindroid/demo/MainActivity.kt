package com.kotlindroid.demo

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kotlindroid.mvp.LifecycleMvp
import com.kotlindroid.mvp.SubMode
import com.kotlindroid.rxbus.RxBus
import com.kotlindroid.rxbus.RxBusEvent
import com.kotlindroid.vitals.bind
import com.kotlindroid.vitals.err
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

class MainActivity : LifecycleMvp.Surviving.Rx.Activity<PresenterImpl>(), MyView {
    val textView by bind<TextView>(R.id.text)
//    val wrapper = Wrapper { showText(it);"RECEIVING".err }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
//        wrapper.subscribeTo(lifecycle)
        findViewById<Button>(R.id.start).setOnClickListener { onClick() }
    }

    override fun providePresenter() = PresenterImpl().apply { "new presenter".err }

    override fun onClick() {
        presenter?.startCountdown()
    }

    override fun showText(text: String) {
        runOnUiThread { textView.text = text }
    }
}

class PresenterImpl: LifecycleMvp.Surviving.Rx.Presenter<MyView>() {
    fun startCountdown() {
        /*addSubscription(*/Observable.interval(1000,TimeUnit.MILLISECONDS).subscribe {
            "UPDATING".err
//            view?.showText(it.toString())
            RxBus.send(event = TextChange(it.toString()))
        }//)
    }
}

//class Wrapper(val action: (text: String) -> Unit): RxBusSubscriptionManager {
//    init {
//        RxBus.subscribe(subscriber = this, observer = Consumer {
//            if (it is TextChange)
//                action(it.content)
//        })
//    }
//}

interface MyView : LifecycleMvp.View {
    fun onClick()
    fun showText(text: String)
}

class TextChange(override val content: String) : RxBusEvent
