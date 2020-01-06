package com.don11995.shell

import com.don11995.log.SimpleLog

object Shell {

    @JvmStatic
    fun execute(command: ShellCommand): ShellResult =
            try {
                ShellCallable(command).call()
            } catch (e: Exception) {
                SimpleLog.e(e)
                ShellResult(false, null)
            }

}
