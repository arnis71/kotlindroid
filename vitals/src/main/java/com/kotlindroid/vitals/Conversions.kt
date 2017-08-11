package com.kotlindroid.vitals

/**
 * Created by arnis on 8/11/2017.
 */

val Float.d : Double
    get() = toDouble()
val Float.s : String
    get() = toString()
val Float.i : Int
    get() = toInt()

val Double.f : Float
    get() = toFloat()
val Double.s : String
    get() = toString()
val Double.i : Int
    get() = toInt()

val String.d : Double
    get() = toDouble()
val String.f : Float
    get() = toFloat()
val String.i : Int
    get() = toInt()