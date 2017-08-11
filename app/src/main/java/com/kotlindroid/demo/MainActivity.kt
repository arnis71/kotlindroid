package com.kotlindroid.demo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kotlindroid.mvp.LifecycleMvp
import com.kotlindroid.vitals.bind
import com.kotlindroid.vitals.err
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainActivity : LifecycleMvp.Surviving.Rx.Activity<PresenterImpl>(), MyView {
    val textView by bind<TextView>(R.id.text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
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
        addSubscription(Observable.interval(1000,TimeUnit.MILLISECONDS).subscribe {
            "UPDATING".err
            view?.showText(it.toString())
        })
    }
}

interface MyView : LifecycleMvp.View {
    fun onClick()
    fun showText(text: String)
}
