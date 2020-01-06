package com.don11995.filter

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class VariantFilter : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        val ext = extensions.create(EXTENSION_NAME, VariantFilterExtension::class.java)
        configure<AppExtension> {
            variantFilter {
                val buildVariant = ext.currentBuildVariant ?: ""
                if (buildVariant.isEmpty()) return@variantFilter
                if (name != buildVariant) {
                    setIgnore(true)
                    println("Skip variant: $name")
                }
            }
        }
    }

    companion object {
        private const val EXTENSION_NAME = "buildFilter"
    }
}
