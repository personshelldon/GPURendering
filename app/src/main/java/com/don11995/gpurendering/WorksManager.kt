package com.don11995.gpurendering

import androidx.work.OneTimeWorkRequest

class WorksManager {

    val enableGpuRenderingRequest: OneTimeWorkRequest
        get() = OneTimeWorkRequest.from(GPURenderingWorker::class.java)

    companion object {
        const val ENABLE_GPU_RENDERING_JOB_TAG: String = "EnableGPURendering"
    }
}