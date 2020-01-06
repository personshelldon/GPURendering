package com.don11995.utils

infix fun Int.untilDown(to: Int): IntRange {
    if (to >= Int.MAX_VALUE) return IntRange.EMPTY
    return this..(to + 1)
}
