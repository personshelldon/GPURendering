package com.don11995.security

import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.regex.Pattern
import java.util.zip.ZipFile

private const val ASSEMBLE_TASK_PREFIX = "assemble"
private const val BUNDLE_TASK_PREFIX = "bundle"
private const val CRC_TASK_PREFIX = "calculateCrc"
private const val INT_MASK = 0xFFFFFFFF.toInt()
private const val BUFFER_SIZE = 16_384

const val CRC_CREATION_TASK: String = "${CRC_TASK_PREFIX}CreateDefaultFile"

private val patternClassesDex = Pattern.compile("^((base.+)?classes[0-9]*[.]dex)")

@Suppress("DefaultLocale")
fun ApplicationVariant.getCrcBundleTaskName(): String =
        CRC_TASK_PREFIX +
                BUNDLE_TASK_PREFIX.capitalize() +
                name.capitalize()

@Suppress("DefaultLocale")
fun ApplicationVariant.getCrcAssembleTaskName(): String =
        CRC_TASK_PREFIX +
                ASSEMBLE_TASK_PREFIX.capitalize() +
                name.capitalize()

fun Project.createCrcCreationTask(
        extension: SecurityExtension
): Task = tasks.create(CRC_CREATION_TASK).doLast {
    if (extension.crcFilePath.isEmpty() ||
            extension.crcFormatString.isEmpty()) {
        println("crcFilePath or crcFormatString is not set. Skip CRC calculation")
        return@doLast
    }
    val crcFile = File(extension.crcFilePath)
    if (!crcFile.isFile) {
        crcFile.delete()
        check(crcFile.createNewFile()) {
            "Can not create file: $crcFile"
        }
        val finalString = String.format(extension.crcFormatString, "0x0")
        val fileOutputStream = FileOutputStream(crcFile)
        fileOutputStream.write(finalString.toByteArray(Charset.defaultCharset()))
        fileOutputStream.close()
        println("Default CRC file initialized!")
    }
}

fun Project.createCrcTask(
        taskName: String,
        outputFile: File,
        extension: SecurityExtension
): Task = tasks.create(taskName).doLast {
    if (extension.crcFilePath.isEmpty() ||
            extension.crcFormatString.isEmpty()) {
        println("crcFilePath or crcFormatString is not set. Skip CRC calculation")
        return@doLast
    }
    println("File to calculate CRC: $outputFile")
    val crcFile = File(extension.crcFilePath)

    var result = 0
    val crcFileParent = crcFile.parentFile
    val zipFile = ZipFile(outputFile)

    for (entry in zipFile.entries()) {
        if (!patternClassesDex.matcher(entry.name).matches()) continue
        result += entry.crc.toInt()
        println("Zip entry: ${entry.name}; CRC: ${String.format("0x%08X", entry.crc)}")
    }

    zipFile.close()

    val crcString = String.format("0x%08X", result and INT_MASK)
    val finalString = String.format(extension.crcFormatString, crcString)

    println("Final CRC calculated: $crcString")

    check(crcFileParent.isDirectory || crcFileParent.mkdirs()) {
        "Can not create folder: $crcFileParent"
    }

    if (crcFile.isFile) {
        val fileInputStream = FileInputStream(crcFile)
        val reader = BufferedReader(InputStreamReader(fileInputStream))
        val data = StringBuilder()
        val buffer = CharArray(BUFFER_SIZE)
        var read: Int
        do {
            read = reader.read(buffer)
            if (read < 0) break
            data.append(buffer, 0, read)
        } while (read >= 0)
        reader.close()
        if (finalString == data.toString()) {
            println("CRC is valid! No rebuild needed!")
            return@doLast
        }
        check(crcFile.delete() && crcFile.createNewFile()) {
            "Can not recreate file: $crcFile"
        }
    } else {
        crcFile.delete()
        check(crcFile.createNewFile()) {
            "Can not create file: $crcFile"
        }
    }
    println("Need rebuild due to CRC change! New CRC: $crcString")
    extension.isRebuildNeeded = true
    val fileOutputStream = FileOutputStream(crcFile)
    fileOutputStream.write(finalString.toByteArray(Charset.defaultCharset()))
    fileOutputStream.close()
    println("CRC printed to file: $crcFile")
}




