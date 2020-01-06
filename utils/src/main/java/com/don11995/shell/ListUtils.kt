package com.don11995.shell

import java.util.*

internal object ListUtils {

    fun getPrimitiveArray(array: List<Byte>): ByteArray {
        if (array.isEmpty()) return ByteArray(0)
        val result = ByteArray(array.size)
        for (i in result.indices) {
            result[i] = array[i]
        }
        return result
    }

    fun getObjectArray(array: ByteArray): List<Byte> {
        val result = ArrayList<Byte>()
        if (array.isEmpty()) return result
        for (b in array) {
            result.add(b)
        }
        return result
    }
}
