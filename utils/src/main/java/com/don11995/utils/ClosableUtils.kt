package com.don11995.utils

import java.io.Closeable
import java.io.Flushable
import java.io.IOException

fun Closeable?.safeClose() {
    if (this == null) return
    try {
        if (this is Flushable) {
            this.flush()
        }
    } catch (ignored: IOException) {
    }

    try {
        this.close()
    } catch (ignored: IOException) {
    }
}

fun Process?.safeDestroy() {
    if (this == null) return
    try {
        this.destroy()
    } catch (e: Throwable) {
    }
}
