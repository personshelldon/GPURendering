package com.don11995.utils

import com.don11995.log.SimpleLog

@Suppress("MemberVisibilityCanBePrivate", "unused")
class TimeTrace internal constructor(val tag: String, val log: String? = null) {

    val startTime: Long = System.currentTimeMillis()

    private var previousCheckPoint = startTime

    fun checkPoint(tag: String? = null) {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - previousCheckPoint
        previousCheckPoint = currentTime

        val localTag = if (tag == null) {
            this.tag
        } else {
            "${this.tag}:$tag"
        }

        SimpleLog.d("Checkpoint $localTag took $diff ms")
    }

    fun end() {
        val output = StringBuilder(
                "Block $tag took ${System.currentTimeMillis() - startTime} ms"
        )
        log?.let {
            output.append(": $it")
        }
        SimpleLog.d(output.toString())
    }

    companion object {
        fun startTrace(tag: String, log: String? = null): TimeTrace = TimeTrace(tag, log)

        fun <T> trace(tag: String, log: String? = null, block: TimeTrace.() -> T): T {
            val tracer = startTrace(tag, log)
            val result = tracer.block()
            tracer.end()
            return result
        }
    }
}

