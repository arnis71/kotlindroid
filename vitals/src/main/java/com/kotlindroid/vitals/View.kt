package com.kotlindroid.vitals

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by arnis on 05/08/2017.
 */

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<T>(idRes)}
}

fun Activity.bindString(@IdRes idRes: Int): Lazy<String> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { resources.getString(idRes) as String }
}

fun View.bindString(@IdRes idRes: Int): Lazy<String> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { resources.getString(idRes) as String }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun View.inflate(resource: Int): View {
    return LayoutInflater.from(context).inflate(resource,null)
}

fun ViewGroup.inflateLayout(resource: Int): View {
    return View.inflate(context,resource,this)
}

@Suppress("UNCHECKED_CAST")
inline fun <T: View> ViewGroup.forChild(block: (child: T)->Unit){
    if (childCount>0)
        for (i in 0..childCount-1)
            block(getChildAt(i) as T)
}

inline fun ViewGroup.forChildIndexed(code: (child: View, index: Int)->Unit){
    if (childCount>0)
        for (i in 0..childCount-1)
            code.invoke(getChildAt(i), i)
}

@Suppress("UNCHECKED_CAST")
operator fun <T : View> ViewGroup.get(index: Int): T? {
    getChildAt(index)?.let {
        return it as T
    } ?: return null
}

@Suppress("UNCHECKED_CAST")
fun <T : AppCompatActivity> Fragment.getActivity(): T = activity as T

inline fun View.onFling(crossinline action: (direction: String) -> Unit) {
    val gd = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, v1: Float, v2: Float): Boolean {
            action(e1.getSlope(e2))
            return true
        }
    })
    setOnTouchListener { _, motionEvent -> gd.onTouchEvent(motionEvent); true }
}

fun MotionEvent.getSlope(otherEvent: MotionEvent) = when (Math.toDegrees(Math.atan2((x - otherEvent.y).toDouble(), (otherEvent.x - y).toDouble()))) {
    in 46..135 -> "up"
    in 135..180 -> "left"
    in -44..45 -> "right"
    in -134..-46 -> "down"
    in -180..-135 -> "left"
    else -> ""
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}
fun View.gone(){
    visibility = View.GONE
}

fun ImageView.setImageByteArray(byteArray: ByteArray, scaleX: Int = 0, scaleY: Int = 0){
    var bitmap = byteArray.toBitmap()
    if (scaleX != 0 || scaleY != 0)
        bitmap = Bitmap.createScaledBitmap(bitmap,scaleX,scaleY,false)

    setImageBitmap(bitmap)
}

fun ImageView.setTransitionNameCompat(name: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        transitionName = name
}

fun ImageView.getTransitionNameCompat() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) transitionName else ""

fun ImageView.getImageByteArray() : ByteArray = (drawable as BitmapDrawable).bitmap.toByteArray()

fun AppCompatActivity.bitmapFromRes(resId: Int) = BitmapFactory.decodeResource(resources,resId)

//fun Context.dpToPx(value: Int) = resources.displayMetrics.density * value
val View.screenHeight: Int
    get() = resources.displayMetrics.heightPixels
val View.screenWidth: Int
    get() = resources.displayMetrics.widthPixels
val View.density: Float
    get() = resources.displayMetrics.density

fun Activity.getStringRes(resId: Int) = resources.getString(resId)
fun Context.getStringRes(resId: Int) = resources.getString(resId)
fun Fragment.getStringRes(resId: Int) = resources.getString(resId)

fun TextView.setTextRes(resId: Int) {
    text = resources.getString(resId)
}

inline fun View.onPreDraw(crossinline block: EmptyBlock) {
    viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            block()
            return true
        }
    })
}

inline fun View.onLayout(crossinline block: EmptyBlock) {
    viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            block()
        }
    })
}