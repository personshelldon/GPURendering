package com.don11995.security

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import java.io.File

fun Project.setUpRebuildIfNeeded(ext: SecurityExtension) {
    gradle.buildFinished {
        val binName = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> "gradlew.bat"
            Os.isFamily(Os.FAMILY_UNIX) -> "gradlew"
            else -> error("Can not finish build on this system! Use Windows or Linux!")
        }
        val gradleBat = File(rootDir.canonicalPath, binName).path
        val createReleaseSourcesTask = tasks.findByName(CREATE_RELEASE_SOURCES_TASK)
        requireNotNull(createReleaseSourcesTask) {
            "Task $CREATE_RELEASE_SOURCES_TASK not found!"
        }
        if (createReleaseSourcesTask.state.executed
                && createReleaseSourcesTask.state.failure == null) {
            exec {
                executable = gradleBat
                args = listOf(RESTORE_SOURCES_TASK)
            }
        }
        if (ext.isRebuildNeeded) {
            exec {
                executable = gradleBat
                args = getCurrentCommand()
            }
        }
    }
}

private fun Project.getCurrentCommand(): List<String> {
    val props = ArrayList<String>()
    val gradle = gradle
    val tasks = gradle.startParameter.taskRequests
    for (task in tasks) {
        for (arg in task.args) {
            props.add(arg)
        }
    }
    val projectProperties = gradle.startParameter.projectProperties
    for ((key, value) in projectProperties) {
        props.add("-P$key=\"$value\"")
    }
    props.add("-Dorg.gradle.configureondemand=false")
    return props
}
