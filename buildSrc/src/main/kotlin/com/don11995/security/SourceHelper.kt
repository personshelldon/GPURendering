package com.don11995.security

import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

const val RESTORE_SOURCES_TASK: String = "restoreSources"
const val CREATE_RELEASE_SOURCES_TASK: String = "createReleaseSources"

private const val BACKUP_SOURCES_TASK = "backupSources"
private const val DELETE_BACKUP_TASK = "deleteBackup"
private const val BACKUP_SOURCES_FOLDER = "/build/backup"

fun Project.createBackupSourcesTask(): Task =
        tasks.create(BACKUP_SOURCES_TASK).doLast {
            val projects = rootProject.childProjects
            for ((_, project) in projects) {
                val path = project.projectDir.absolutePath
                copy {
                    from("$path/src")
                    into("$path$BACKUP_SOURCES_FOLDER")
                    include("**/*.java", "**/*.kt")
                }
            }
            println("Source files backed up!")
        }

fun Project.createRestoreSourcesTask(): Task =
        tasks.create(RESTORE_SOURCES_TASK).doLast {
            val projects = rootProject.childProjects
            for ((_, project) in projects) {
                val path = project.projectDir.absolutePath
                copy {
                    from("$path$BACKUP_SOURCES_FOLDER")
                    into("$path/src")
                    include("**/*.java", "**/*.kt")
                }
            }
            println("Source files restored!")
        }

fun Project.createDeleteBackupTask(): Task =
        tasks.create(DELETE_BACKUP_TASK).doLast {
            val projects = rootProject.childProjects
            for ((_, project) in projects) {
                val path = project.projectDir.absolutePath
                delete("$path$BACKUP_SOURCES_FOLDER")
            }
            println("Backup of sources deleted!")
        }

fun Project.createReleaseSourcesTask(
        ext: SecurityExtension
): Task = tasks.create(CREATE_RELEASE_SOURCES_TASK).doLast {
    val projects = rootProject.childProjects
    for ((_, project) in projects) {
        val path = project.projectDir.absolutePath
        val srcFiles = fileTree("$path/src") {
            include("**/*.java", "**/*.kt")
        }
        val logImport = "import android.util.Log"
        val simpleLogImport = "import com.don11995.log.SimpleLog"
        val valueMapperImport = "import com.don11995.log.ValueMapper"
        srcFiles.forEach {
            var content = it.getText()
            val isKotlin = it.path.endsWith("kt")
            content = removeMethodCall(content,
                    simpleLogImport,
                    "SimpleLog",
                    isKotlin,
                    ext.deleteErrorLogs)
            content = removeMethodCall(content,
                    valueMapperImport,
                    "ValueMapper",
                    isKotlin,
                    ext.deleteErrorLogs)
            content = removeMethodCall(content,
                    logImport,
                    "Log",
                    isKotlin,
                    ext.deleteErrorLogs)
            it.setText(content)
        }
    }
    println("All logs deleted!")
}

private fun removeMethodCall(
        source: String,
        importLib: String,
        startsWith: String,
        isKotlin: Boolean,
        deleteErrors: Boolean
): String {
    var index: Int
    var result = source
    var searchIndex = 0
    if (result.contains(importLib)) {
        do {
            index = result.indexOf("$startsWith.", searchIndex)
            if (index < 0) break
            val dotIndex = result.indexOf('.', index)
            if (dotIndex < index) break
            val firstChar = result[dotIndex + 1]
            if (Character.isUpperCase(firstChar)) {
                searchIndex = dotIndex + 1
                continue
            }
            if (!deleteErrors) {
                val braceIndex = result.indexOf('(', dotIndex)
                if (braceIndex < dotIndex) break
                val callName = result.substring(dotIndex + 1, braceIndex)
                if (callName.isErrorCall()) {
                    searchIndex = braceIndex
                    continue
                }
            }
            var openBraceCount = 0
            var closeBraceCount = 0
            val startIndex = index
            var endIndex: Int
            while (true) {
                val a = result[index++]
                if (a == '(') openBraceCount++
                if (a == ')') closeBraceCount++
                endIndex = index
                if (openBraceCount == 0 && closeBraceCount == 0) continue
                if (openBraceCount == closeBraceCount) break
            }
            val textToReplace = result.substring(startIndex, endIndex)
            println("Remove method call: $textToReplace")
            val replaceBy = if (isKotlin) "Unit" else "{}"
            result = result.replace(textToReplace, replaceBy)
        } while (index >= 0)
    }
    return result
}

private fun String.isErrorCall(): Boolean =
        this == "e" || this == "fe" || this == "te" || this == "tfe"

private fun File.getText(): String = ResourceGroovyMethods.getText(this)

private fun File.setText(text: String) = ResourceGroovyMethods.setText(this, text)

