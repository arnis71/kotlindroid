package com.kotlindroid.vitals

import kotlin.properties.Delegates

/**
 * Created by arnis on 05/08/2017.
 */

inline fun <T> Any.observableList(crossinline onChange: (oldValue: MutableList<T>, newValue: MutableList<T>) -> Unit) =
    Delegates.observable(mutableListOf<T>()){ _, oldValue: MutableList<T>, newValue: MutableList<T> ->
        onChange(oldValue, newValue)
    }

inline fun <T> observableValue(initValue: T,crossinline onChange: (oldValue: T, newValue: T) -> Unit) =
    Delegates.observable(initValue){ _, oldValue: T, newValue: T ->
        onChange(oldValue, newValue)
    }