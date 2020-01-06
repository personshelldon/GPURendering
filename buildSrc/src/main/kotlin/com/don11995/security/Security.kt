package com.don11995.security

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Suppress("unused")
class Security : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        val ext = extensions.create(EXTENSION_NAME, SecurityExtension::class.java)
        createTasks(ext)
        setUpRebuildIfNeeded(ext)
        configure<AppExtension> {
            applicationVariants.all {
                configureNamingAndCrc(target, ext)
                configureDefaultCrcAndReleaseSources(target)
            }
        }
    }

    private fun Project.createTasks(ext: SecurityExtension) {
        createCrcCreationTask(ext)
        createRestoreSourcesTask()
        createDeleteBackupTask()
        createGenerateDictionaryTask(ext)
        val createReleaseSourcesTask = createReleaseSourcesTask(ext)
        val backupSourcesTask = createBackupSourcesTask()
        createReleaseSourcesTask.dependsOn(backupSourcesTask)
    }

    @Suppress("DefaultLocale")
    private fun ApplicationVariant.configureDefaultCrcAndReleaseSources(
            project: Project
    ) {
        val preBuildTaskName = "$PRE_BUILD_TASK_PREFIX${name.capitalize()}$PRE_BUILD_TASK_SUFFIX"
        val preBuildTask = project.tasks.findByName(preBuildTaskName)
        requireNotNull(preBuildTask) { "Can not find task $preBuildTaskName" }

        val crcCreationTask = project.tasks.findByName(CRC_CREATION_TASK)
        requireNotNull(crcCreationTask) { "Can not find task $CRC_CREATION_TASK" }

        preBuildTask.dependsOn(crcCreationTask)

        if (!name.endsWith(RELEASE_SUFFIX, true)) {
            return
        }

        val createReleaseSourcesTask = project.tasks.findByName(CREATE_RELEASE_SOURCES_TASK)
        requireNotNull(createReleaseSourcesTask) {
            "Can not find task $CREATE_RELEASE_SOURCES_TASK"
        }

        preBuildTask.dependsOn(createReleaseSourcesTask)

    }

    @Suppress("DefaultLocale")
    private fun ApplicationVariant.configureNamingAndCrc(
            project: Project,
            ext: SecurityExtension
    ) {
        if (!name.endsWith(RELEASE_SUFFIX, true)) {
            return
        }

        var tempFile: File? = null
        outputs.all {
            tempFile = outputFile
        }
        val apkOutFile = tempFile
                ?: throw IllegalArgumentException("\"Variant $name has no outputs!\"")
        val outputFolder = apkOutFile.parentFile
        val aabName = apkOutFile.name.replace(APK_EXTENSION, AAB_EXTENSION)
        val aabOutFile = File(outputFolder, aabName)

        val bundleTaskName = "$BUNDLE_TASK_PREFIX${name.capitalize()}"
        val bundleTask = project.tasks.findByName(bundleTaskName)
        requireNotNull(bundleTask) { "Can not find task $bundleTaskName" }

        val assembleTaskName = "$ASSEMBLE_TASK_PREFIX${name.capitalize()}"
        val assembleTask = project.tasks.findByName(assembleTaskName)
        requireNotNull(assembleTask) { "Can not find task $assembleTaskName" }

        val crcAssembleTask = project.createCrcTask(
                getCrcAssembleTaskName(),
                apkOutFile,
                ext
        )
        val crcBundleTask = project.createCrcTask(
                getCrcBundleTaskName(),
                aabOutFile,
                ext
        )

        bundleTask.finalizedBy(crcBundleTask)
        assembleTask.finalizedBy(crcAssembleTask)
    }

    private fun Project.createBundleRenameTask(
            taskName: String,
            sourcePath: String,
            targetPath: String
    ): Task = tasks.create(taskName).doLast {
        val finalAabFolder = File(targetPath).parentFile
        check(finalAabFolder.isDirectory || finalAabFolder.mkdirs()) {
            "Can not create folder $finalAabFolder"
        }
        val srcPath = Paths.get(sourcePath)
        val dstPath = Paths.get(targetPath)
        Files.move(srcPath, dstPath, StandardCopyOption.REPLACE_EXISTING)
    }

    @Suppress("DefaultLocale")
    private fun ApplicationVariant.getBundleRenameTaskName(): String =
            "$BUNDLE_TASK_PREFIX${name.capitalize()}$RENAME_TASK_SUFFIX"

    companion object {
        const val EXTENSION_NAME: String = "security"
        private const val PRE_BUILD_TASK_PREFIX = "pre"
        private const val PRE_BUILD_TASK_SUFFIX = "Build"
        private const val DEFAULT_APP_NAME = "App"
        private const val BUNDLE_TASK_PREFIX = "bundle"
        private const val ASSEMBLE_TASK_PREFIX = "assemble"
        private const val RELEASE_SUFFIX = "Release"
        private const val RENAME_TASK_SUFFIX = "Rename"
        private const val APK_EXTENSION = ".apk"
        private const val AAB_EXTENSION = ".aab"
    }
}
