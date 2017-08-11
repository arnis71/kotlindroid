package com.kotlindroid.mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by arnis on 06/08/2017.
 */
enum class SubMode {
    REMOVE_ON_BACK_PRESSED,
    REMOVE_ON_STOP
}

sealed class LifecycleMvp {
    interface View

    sealed class Surviving {
        abstract class Presenter<VIEW: LifecycleMvp.View>: LifecycleEvents {
            var view: VIEW? = null
                private set

            @Suppress("UNCHECKED_CAST")
            internal fun bind(view: LifecycleMvp.View) {
                this.view = view as VIEW
            }

            override fun onDetach() {
                view = null
            }
        }

        abstract class Activity<PRESENTER: Presenter<*>>: LifecycleActivity() {
            var presenter: PRESENTER? = null

            @Suppress("UNCHECKED_CAST")
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                ((lastCustomNonConfigurationInstance as? PRESENTER)?: providePresenter()).let {
                    presenter = it
                    bind(it)
                    lifecycle.addObserver(it)
                }
            }

            override fun onRetainCustomNonConfigurationInstance() = presenter

            private fun bind(presenter: PRESENTER) {
                if (this is LifecycleMvp.View && presenter is Presenter<*>)
                    presenter.bind(this)
            }

            abstract fun providePresenter(): PRESENTER
        }

        sealed class Rx {
            abstract class Presenter<VIEW: LifecycleMvp.View>(var unsubscribeMode: SubMode = SubMode.REMOVE_ON_BACK_PRESSED): LifecycleMvp.Surviving.Presenter<VIEW>() {
                internal val subscriptions = CompositeDisposable()
                fun addSubscription(d: Disposable)  = subscriptions.add(d)

                override fun onDetach() {
                    super.onDetach()
                    if (unsubscribeMode == SubMode.REMOVE_ON_STOP)
                        subscriptions.clear()
                }

                internal fun onAppExit() {
                    if (unsubscribeMode == SubMode.REMOVE_ON_BACK_PRESSED)
                        subscriptions.clear()
                }
            }

            abstract class Activity<PRESENTER: Presenter<*>>: LifecycleMvp.Surviving.Activity<PRESENTER>() {
                override fun onBackPressed() {
                    presenter?.onAppExit()
                    super.onBackPressed()
                }
            }
        }
    }

    abstract class Presenter<VIEW: LifecycleMvp.View>(var view: VIEW?): LifecycleEvents {
        override fun onDetach() {
            view = null
        }
    }

    abstract class Activity<PRESENTER: Presenter<*>>: LifecycleActivity() {
        var presenter: PRESENTER? = null

        @Suppress("UNCHECKED_CAST")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            providePresenter().takeIf { this is LifecycleMvp.View }?.let {
                presenter = it
                lifecycle.addObserver(it)
            }
        }

        override fun onDestroy() {
            presenter = null
            super.onDestroy()
        }

        abstract fun providePresenter(): PRESENTER
    }
}

interface LifecycleEvents: LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAttach() {}
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onDetach()
}

