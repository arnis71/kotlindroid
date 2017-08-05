package com.kotlindroid.vitals

import android.util.Log

/**
 * Created by arnis on 05/08/2017.
 */

fun <T> T.log(header: String): T {
    Log.e("happy",header)
    Log.d("happy",this.toString())
    return this
}

val String.log: String
    get() {
        Log.d("happ", this)
        return this
    }

val String.err: String
    get() {
        Log.e("happ", this)
        return this
    }