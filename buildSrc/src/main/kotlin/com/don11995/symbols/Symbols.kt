package com.don11995.symbols

import com.android.build.gradle.AppExtension
import com.android.build.gradle.tasks.ExternalNativeBuildTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Suppress("unused")
class Symbols : Plugin<Project> {

    @Suppress("DefaultLocale")
    override fun apply(target: Project): Unit = target.run {
        val extension = extensions.create(
                EXTENSION_NAME,
                SymbolsExtension::class.java
        )
        val deleteSymbolsTask = createDeleteSymbolsTaskIdNeeded(extension)
        val appExtension = extensions.findByType(AppExtension::class.java)
        appExtension?.let {
            it.applicationVariants.all {
                val preBuildTaskName = PRE_BUILD_TASK_PREFIX +
                        name.capitalize() +
                        PRE_BUILD_TASK_SUFFIX
                val preBuildTask = project.tasks.findByName(preBuildTaskName)
                requireNotNull(preBuildTask) { "Can not find task $preBuildTaskName" }

                preBuildTask.dependsOn(deleteSymbolsTask)
            }
        }
        tasks.withType<ExternalNativeBuildTask> {
            if (!name.endsWith(RELEASE_SUFFIX, true)) {
                return@withType
            }
            val dest = extension.destination
            requireNotNull(dest) { "Destination for symbols is not set!" }
            val newTaskName = "${name}${TASK_NAME_SUFFIX}"
            val newTask = createCopySymbolsTask(
                    newTaskName,
                    this,
                    dest
            )
            this.finalizedBy(newTask)
        }
    }

    private fun copyFolder(src: Path, dest: Path) {
        Files.walk(src).forEach { source ->
            copy(source, dest.resolve(src.relativize(source)))
        }
    }

    private fun copy(source: Path, dest: Path) {
        if (Files.isDirectory(source) && Files.isDirectory(dest)) return
        Files.deleteIfExists(dest)
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING)
    }

    private fun Project.createCopySymbolsTask(
            name: String,
            externalNativeBuildTask: ExternalNativeBuildTask,
            targetFile: File
    ): Task = tasks.create(name).doLast {
        val symbolsFolder = externalNativeBuildTask.objFolder
        val symbolsPath = symbolsFolder.canonicalPath
        val targetPath = targetFile.canonicalPath
        check(symbolsFolder.exists()) {
            "Folder is missing: ${symbolsFolder.path}"
        }
        check(targetFile.exists() || targetFile.mkdirs()) {
            "Can not create folder: $targetPath"
        }
        println("Copy files from $symbolsPath to $targetPath")
        val from = Paths.get(symbolsPath)
        val to = Paths.get(targetPath)
        copyFolder(from, to)
    }

    private fun Project.createDeleteSymbolsTaskIdNeeded(
            ext: SymbolsExtension
    ): Task {
        val task = tasks.findByName(DELETE_SYMBOLS_TASK)
        return task ?: tasks.create(DELETE_SYMBOLS_TASK).doLast {
            val dest = ext.destination
            requireNotNull(dest) { "Destination for symbols is not set!" }

            delete(dest.absolutePath)
        }
    }

    companion object {
        private const val PRE_BUILD_TASK_PREFIX = "pre"
        private const val PRE_BUILD_TASK_SUFFIX = "Build"
        private const val DELETE_SYMBOLS_TASK = "deleteSymbolsFolder"
        private const val EXTENSION_NAME = "symbols"
        private const val TASK_NAME_SUFFIX = "CopySymbols"
        private const val RELEASE_SUFFIX = "Release"
    }
}
