package com.don11995.gpurendering

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val workManager by inject<WorkManager>()
    private val worksManager by inject<WorksManager>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED != intent?.action ||
            context == null
        ) {
            return
        }

        workManager.enqueueUniqueWork(
            WorksManager.ENABLE_GPU_RENDERING_JOB_TAG,
            ExistingWorkPolicy.KEEP,
            worksManager.enableGpuRenderingRequest
        )
    }
}