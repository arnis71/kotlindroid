package com.kotlindroid.demo

import android.app.Application
import com.kotlindroid.rxbus.RxBus

/**
 * Created by arnis on 8/11/2017.
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        RxBus.init()
    }
}