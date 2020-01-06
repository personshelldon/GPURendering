package com.don11995.codestyle

import com.don11995.codestyle.CodeStyle.Companion.setDestination
import com.don11995.codestyle.CodeStyle.Companion.setHtmlEnabled
import com.don11995.codestyle.CodeStyle.Companion.setInput
import com.don11995.codestyle.CodeStyle.Companion.setLintConfig
import com.don11995.codestyle.CodeStyle.Companion.setTxtEnabled
import com.don11995.codestyle.CodeStyle.Companion.setXmlEnabled
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import java.io.File

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class CodeStyleExtension(private val project: Project) {

    private val rootDir = project.rootDir
    private val projectDir = project.projectDir

    var destination: String = "$rootDir/reports"
        set(value) {
            field = value
            project.setDestination(value)
        }

    var isHtmlEnabled: Boolean = true
        set(value) {
            field = value
            project.setHtmlEnabled(value)
        }

    var isXmlEnabled: Boolean = false
        set(value) {
            field = value
            project.setXmlEnabled(value)
        }

    var isTxtEnabled: Boolean = false
        set(value) {
            field = value
            project.setTxtEnabled(value)
        }

    var input: ConfigurableFileCollection = project.run {
        files("$projectDir/src", "$rootDir/buildSrc/src")
    }
        set(value) {
            field = value
            project.setInput(value)
        }

    var lintConfig: File = File(projectDir, "lint.xml")
        set(value) = project.run {
            field = value
            project.setLintConfig(value)
        }
}
