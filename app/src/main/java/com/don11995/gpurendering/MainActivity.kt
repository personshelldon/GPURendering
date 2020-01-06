package com.don11995.gpurendering

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val workManager by inject<WorkManager>()
    private val worksManager by inject<WorksManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workManager.enqueueUniqueWork(
            WorksManager.ENABLE_GPU_RENDERING_JOB_TAG,
            ExistingWorkPolicy.KEEP,
            worksManager.enableGpuRenderingRequest
        )
    }
}