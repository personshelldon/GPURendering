package com.don11995.shell

import java.nio.charset.Charset

@Suppress("CanBeParameter", "unused", "MemberVisibilityCanBePrivate")
class ShellResult internal constructor(
        val isError: Boolean,
        val resultBytes: ByteArray?
) {

    val resultString: String = if (resultBytes == null) {
        ""
    } else {
        String(resultBytes, Charset.defaultCharset()).trim()
    }

}
