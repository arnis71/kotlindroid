package com.kotlindroid.vitals

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by arnis on 05/08/2017.
 */

inline fun <reified T : AppCompatActivity> AppCompatActivity.launch(stringExtra: String? = null, sharedElement: View? = null, transitionName: String?  = null) {
    Intent(this, T::class.java).apply {
        stringExtra?.let { putExtra("stringExtra",it) }
        sharedElement?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@launch, sharedElement, transitionName)
            this@launch.startActivity(intent, options.toBundle())
        } ?: startActivity(intent)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun AppCompatActivity.replaceFragment(container: Int, fragment: Fragment, addToBackStack: Boolean = true) {
    supportFragmentManager.beginTransaction().replace(container,fragment).apply {
        if (addToBackStack)
            addToBackStack(null)
        commit()
    }
}

fun Context.hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
fun Context.hasPermissions(vararg permissions: String): Boolean =
        permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

fun AppCompatActivity.hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
fun AppCompatActivity.hasPermissions(vararg permissions: String): Boolean =
        permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

fun AppCompatActivity.getSharedPrefs(name: String = "db", mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
fun Fragment.getSharedPrefs(name: String = "db", mode: Int = Context.MODE_PRIVATE) = context.getSharedPreferences(name, mode)
fun Context.getSharedPrefs(name: String = "db", mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)

inline fun SharedPreferences.put(runEdit: SharedPreferences.Editor.() -> Unit) {
    edit().apply { runEdit() }.apply()
}

//fun AppCompatActivity.share(source: ByteArray, text: String, shareTitle: String) {
//    val image = source.toBitmap().replaceTransparent(Color.WHITE)
//    val uri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, image, text, ""))
//    Intent().apply {
//        action = Intent.ACTION_SEND
//        putExtra(Intent.EXTRA_TEXT, text)
//        putExtra(Intent.EXTRA_STREAM, uri)
//        type = "image/*"
//        startActivity(Intent.createChooser(this, shareTitle))
//        image.recycle()
//    }
//}