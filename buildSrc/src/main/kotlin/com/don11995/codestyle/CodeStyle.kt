package com.don11995.codestyle

import Plugins
import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import java.io.File

@Suppress("unused")
class CodeStyle : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        apply(plugin = Plugins.detektPlugin)
        val ext = extensions.create(
                EXTENSION_NAME,
                CodeStyleExtension::class.java,
                this
        )
        // Default config
        val detektConfigFile = files("$rootDir" +
                RELATIVE_CONFIG_FOLDER +
                "/$DEFAULT_DETEKT_CONFIG_NAME")
        setDestination(ext.destination)
        setHtmlEnabled(ext.isHtmlEnabled)
        setXmlEnabled(ext.isXmlEnabled)
        setTxtEnabled(ext.isTxtEnabled)
        setInput(ext.input)
        setLintConfig(ext.lintConfig)
        configure<DetektExtension> {
            config = detektConfigFile
            parallel = true
        }
        configure<BaseExtension> {
            lintOptions {
                isWarningsAsErrors = true
                isAbortOnError = true
            }
        }
    }

    companion object {
        private const val DETEKT_REPORT_NAME: String = "detekt"
        private const val LINT_REPORT_NAME: String = "lint-report"
        private const val EXTENSION_NAME = "codestyle"
        private const val RELATIVE_CONFIG_FOLDER = "/buildSrc/src/main/resources/config"
        private const val DEFAULT_DETEKT_CONFIG_NAME = "detekt.yml"

        internal fun Project.setDestination(path: String) {
            configure<DetektExtension> {
                reports.html.destination = file("$path/${DETEKT_REPORT_NAME}.html")
                reports.xml.destination = file("$path/${DETEKT_REPORT_NAME}.xml")
                reports.txt.destination = file("$path/${DETEKT_REPORT_NAME}.txt")
            }
            configure<BaseExtension> {
                lintOptions {
                    htmlOutput = file("$path/${LINT_REPORT_NAME}.html")
                    xmlOutput = file("$path/${LINT_REPORT_NAME}.xml")
                }
            }
        }

        internal fun Project.setHtmlEnabled(enabled: Boolean) {
            configure<DetektExtension> {
                reports.html.enabled = enabled
            }
            configure<BaseExtension> {
                lintOptions.htmlReport = enabled
            }
        }

        internal fun Project.setXmlEnabled(enabled: Boolean) {
            configure<DetektExtension> {
                reports.xml.enabled = enabled
            }
            configure<BaseExtension> {
                lintOptions.xmlReport = enabled
            }
        }

        internal fun Project.setTxtEnabled(enabled: Boolean) {
            configure<DetektExtension> {
                reports.txt.enabled = enabled
            }
        }

        internal fun Project.setInput(files: ConfigurableFileCollection) {
            configure<DetektExtension> {
                input = files
            }
        }

        internal fun Project.setLintConfig(file: File) {
            configure<BaseExtension> {
                lintOptions.lintConfig = file
            }
        }
    }
}

