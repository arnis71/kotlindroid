package com.kotlindroid.vitals

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/**
 * Created by arnis on 05/08/2017.
 */

fun Context.writeFileByByte(name: String, data: String) {
    FileOutputStream(File("${filesDir.absolutePath}/$name")).apply {
        write(data.toByteArray())
        close()
    }
}

fun Context.fileExists(name: String) = File("${filesDir.absolutePath}/$name").exists()
