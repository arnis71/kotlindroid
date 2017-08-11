package com.kotlindroid.mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by arnis on 06/08/2017.
 */

sealed class Mvp {
    interface View

    abstract class SurvivingActivity<PRESENTER: Mvp.RxLifecyclePresenter<*>>: LifecycleActivity() {
        var presenter: PRESENTER? = null

        @Suppress("UNCHECKED_CAST")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            ((lastCustomNonConfigurationInstance as? PRESENTER)?: providePresenter()).let {
                presenter = it
                bind(it)
                lifecycle.addObserver(it)
                Log.d("happy",lifecycle.observerCount.toString())
            }
        }

        override fun onBackPressed() {
            super.onBackPressed()
            presenter?.subscriptions?.clear()
        }

        override fun onRetainCustomNonConfigurationInstance() = presenter

        private fun bind(presenter: PRESENTER) {
            if (this is Mvp.View && presenter is RxLifecyclePresenter<*>)
                presenter.bind(this)
        }

        abstract fun providePresenter(): PRESENTER
    }

    abstract class RxLifecyclePresenter<VIEW: Mvp.View>: LifecycleObserver {
        var view: VIEW? = null
            private set
        internal val subscriptions = CompositeDisposable()

        fun addSusbcription(d: Disposable)  = subscriptions.add(d)

        @Suppress("UNCHECKED_CAST")
        internal fun bind(view: Mvp.View) {
            this.view = view as VIEW
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        open fun onAttach() {}
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        open fun onDetach() {}
    }
}

