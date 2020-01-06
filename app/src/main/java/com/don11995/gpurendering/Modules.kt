package com.don11995.gpurendering

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private val managersModule = module {
    single { WorkManager.getInstance(androidContext()) }
    single { WorksManager() }
}

val allModules: List<Module> = listOf(managersModule)