package com.don11995.gpurendering

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.don11995.log.SimpleLog
import com.don11995.shell.Shell
import com.don11995.shell.ShellCommand
import com.don11995.shell.ShellCommandMode

class GPURenderingWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        SimpleLog.w("Enabling GPU rendering...")
        return if (enableGpuRendering()) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun enableGpuRendering(): Boolean {
        val shellCommand = ShellCommand(
            GPU_RENDERING_COMMAND,
            ShellCommandMode.MODE_SU
        )
        val result = Shell.execute(shellCommand)
        return if (result.isError) {
            SimpleLog.e(result.resultString)
            false
        } else {
            SimpleLog.d(result.resultString)
            true
        }
    }

    companion object {
        private const val GPU_RENDERING_COMMAND = "service call SurfaceFlinger 1008 i32 1"
    }
}