package com.kotlindroid.demo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kotlindroid.mvp.MVP
import com.kotlindroid.vitals.bind
import com.kotlindroid.vitals.err
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainActivity : MVP.SurvivingActivity<PresenterImpl>(), MyView {
    val textView by bind<TextView>(R.id.text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        findViewById<Button>(R.id.start).setOnClickListener { onClick() }
    }

    override fun providePresenter() = PresenterImpl()

    override fun onClick() {
        presenter?.startCountdown()
    }

    override fun showText(text: String) {
        runOnUiThread { textView.text = text }
    }
}

class PresenterImpl: MVP.LifecyclePresenter<MyView>() {
    fun startCountdown() {
        subscriptions.add(Observable.interval(1000,TimeUnit.MILLISECONDS).subscribe {
            "UPDATING".err
            view?.showText(it.toString())
        })
    }

//    override fun onDetach() {
//        disp?.dispose()
//    }
}

interface MyView : MVP.View {
    fun onClick()
    fun showText(text: String)
}
