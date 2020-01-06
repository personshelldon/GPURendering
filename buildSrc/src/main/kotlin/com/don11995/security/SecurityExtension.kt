package com.don11995.security

@Suppress("unused")
open class SecurityExtension {
    var appName: String = ""
    var crcFilePath: String = ""
    var crcFormatString: String = ""
    var deleteErrorLogs: Boolean = true
    var classesDictFilePath: String = ""
    var methodDictFilePath: String = ""
    var dictWordCount: Int = DEFAULT_WORD_COUNT
    var dictWordSize: Int = DEFAULT_WORD_SIZE

    internal var isRebuildNeeded = false

    companion object {
        private const val DEFAULT_WORD_COUNT = 10_000
        private const val DEFAULT_WORD_SIZE = 10
    }
}
