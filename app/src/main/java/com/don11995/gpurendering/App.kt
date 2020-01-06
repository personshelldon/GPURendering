package com.don11995.gpurendering

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            EmptyLogger()
            modules(allModules)
        }
    }
}