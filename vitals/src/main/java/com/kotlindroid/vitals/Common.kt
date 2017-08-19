package com.kotlindroid.vitals

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.ArrayMap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.Exception

/**
 * Created by arnis on 05/08/2017.
 */

typealias EmptyBlock = () -> Unit

val String.color : Int
    get() = Color.parseColor(this)

val now: Long
    get() = System.currentTimeMillis()

class CachedList<out T>(val initializer: () -> List<T>) {
    private var list = initializer()

    fun cached(): List<T> {
        if (list.isEmpty())
            return live()
        else
            return list
    }

    fun live(): List<T> {
        list = initializer()
        return list
    }
}

@TargetApi(19)
fun <K,V> arrayMapOf() = ArrayMap<K,V>()

@TargetApi(19)
fun <K,V> arrayMapOf(vararg values: Pair<K,V>) = ArrayMap<K,V>().apply {
    values.forEach { (key,value) -> put(key, value) }
}

inline fun Boolean.ifTrue(body: EmptyBlock): Unit? {
    if (this)
        return body()
    else
        return null
}
inline fun Boolean.ifFalse(body: EmptyBlock): Unit?{
    if (!this)
        return body()
    else
        return null
}

fun <T> T?.or(obj: T): T {
    if (isNull())
        return obj
    else
        return this!!
}

fun Any?.isNull(): Boolean = this == null
fun Any?.isNotNull(): Boolean = this != null

infix fun <A> A.equal(that: A) = this == that
infix fun <A> A.not(that: A) = this != that

//fun Any.catchIfThrown(block: EmptyBlock, catchBlock: (e: Exception) -> Unit = {}){
//    try {
//        block()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        catchBlock(e)
//    }
//}

inline fun Any.safeFromException(block: EmptyBlock){
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//inline fun fromApi(version: Int, block: EmptyBlock) {
//    if (Build.VERSION.SDK_INT >= version) block()
//}
//inline fun toApi(version: Int, block: EmptyBlock) {
//    if (Build.VERSION.SDK_INT <= version) block()
//}

fun ByteArray.toBitmap() = BitmapFactory.decodeByteArray(this, 0, this.size)

fun ByteArray.toBase64() = Base64.encodeToString(this, Base64.DEFAULT)

fun Drawable.toByteArray(): ByteArray {
    safeFromException {
        return (this as BitmapDrawable).bitmap.toByteArray()
    }
    return byteArrayOf()
}

fun Bitmap.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100): ByteArray {
    safeFromException {
        val stream = ByteArrayOutputStream()
        compress(format, quality, stream)
        return stream.toByteArray()
    }
    return byteArrayOf()
}

fun Bitmap.replaceTransparent(color: Int) = Bitmap.createBitmap(width,height,config).apply {
    eraseColor(color)
    Canvas(this).drawBitmap(this@replaceTransparent,0f,0f,null)
}