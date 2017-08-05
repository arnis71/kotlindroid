package com.kotlindroid.vitals

import android.os.Handler
import android.os.Looper

/**
 * Created by arnis on 05/08/2017.
 */

fun Any.runAsync(action: EmptyBlock) = Thread(Runnable(action)).start()

fun Any.runOnUiThread(action: EmptyBlock) {
    if (isMainLooperAlive())
        action()
    else
        Handler(Looper.getMainLooper()).post(Runnable(action))
}
fun Any.runDelayed(delayMillis: Long, action: EmptyBlock) = Handler().postDelayed(Runnable(action), delayMillis)

fun Any.runDelayedOnUiThread(delayMillis: Long, action: EmptyBlock) = Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)

private fun isMainLooperAlive() = Looper.myLooper() == Looper.getMainLooper()
