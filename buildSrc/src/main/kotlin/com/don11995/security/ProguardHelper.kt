package com.don11995.security

import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File
import java.io.FileOutputStream
import java.util.*

const val GENERATE_DICT_TASK_NAME: String = "generateProguardDictionaries"

private const val CHARS = "abcdefghijklmnopqrstuvxyz"
private const val NUMBERS = "0123456789"
@Suppress("DefaultLocale")
private val ALPHABETIC = CHARS + CHARS.toUpperCase()
private val DATA_ARRAY = ALPHABETIC + NUMBERS

fun Project.createGenerateDictionaryTask(
        ext: SecurityExtension
): Task = tasks.create(GENERATE_DICT_TASK_NAME).doLast {
    val classesDictPath = ext.classesDictFilePath
    val methodDictPath = ext.methodDictFilePath
    val wordCount = ext.dictWordCount
    val wordSize = ext.dictWordSize

    generateDictionary(classesDictPath, wordCount, wordSize)
    generateDictionary(methodDictPath, wordCount, wordSize)
}

private fun generateDictionary(path: String, wordCount: Int, wordSize: Int) {
    if (path.isEmpty()) {
        println("Classes dictionary path is not set. ProGuard dictionary will not be created")
        return
    }
    require(wordCount > 0 && wordSize > 0) {
        "Value of classesDictCount or classesDictWordSize is incorrect"
    }

    val classDictFile = File(path)
    if (classDictFile.exists()) {
        classDictFile.delete()
        check(classDictFile.createNewFile()) {
            "Can not create file: $classDictFile"
        }
    }

    val os = FileOutputStream(classDictFile)
    val random = Random()
    val builder = StringBuilder()
    var wordCounter = 0
    var charCounter = 0
    while (wordCounter < wordCount) {
        builder.append(ALPHABETIC[random.nextInt(ALPHABETIC.length)])
        charCounter++
        while (charCounter < wordSize) {
            builder.append(DATA_ARRAY[random.nextInt(DATA_ARRAY.length)])
            charCounter++
        }
        builder.append("\r\n")

        os.write(builder.toString().toByteArray())

        charCounter = 0
        builder.clear()
        wordCounter++
    }
    os.close()
}
