package com.don11995.shell

import android.net.LocalSocket
import android.net.LocalSocketAddress
import com.don11995.log.SimpleLog
import com.don11995.utils.safeClose
import com.don11995.utils.safeDestroy
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable

internal class ShellCallable(private val mShellCommand: ShellCommand) : Callable<ShellResult> {

    companion object {
        private const val ERROR_START = "ERROR"
        private val EOF = byteArrayOf('\r'.toByte(),
                '\n'.toByte(),
                '0'.toByte(),
                '\r'.toByte(),
                '\n'.toByte(),
                '\r'.toByte(),
                '\n'.toByte()
        )
    }

    private fun isSUServiceAvailable(): Boolean {
        var socket: LocalSocket? = null
        val initAddress = LocalSocketAddress("suservice",
                LocalSocketAddress.Namespace.RESERVED)
        return try {
            socket = LocalSocket()
            SimpleLog.d("Try detect if SUService available...")
            socket.connect(initAddress)
            SimpleLog.d("SUService available!")
            true
        } catch (e: IOException) {
            SimpleLog.e("SUService is not available...")
            false
        } finally {
            socket.safeClose()
        }
    }

    override fun call(): ShellResult {
        val mode = mShellCommand.mode
        val command = mShellCommand.command
        return if (mode == ShellCommandMode.MODE_SU) {
            if (isSUServiceAvailable()) {
                runCommandInSUService(command)
            } else {
                runCommandInShell(command, true)
            }
        } else {
            runCommandInShell(command, false)
        }
    }

    private fun runCommandInSUService(command: String): ShellResult {
        if (command.isEmpty()) {
            return ShellResult(true, "Command is empty".toByteArray())
        }

        var socket: LocalSocket? = null
        var inputStream: InputStream? = null
        var writer: BufferedWriter? = null
        try {
            val initAddress = LocalSocketAddress("suservice",
                    LocalSocketAddress.Namespace.RESERVED)
            socket = LocalSocket()
            SimpleLog.d("Connecting to SUService...")
            socket.connect(initAddress)
            SimpleLog.d("Connected to SUService!")
            inputStream = socket.inputStream
            val out = socket.outputStream
            val outWriter = OutputStreamWriter(out, Charset.defaultCharset())
            writer = BufferedWriter(outWriter)
            writer.write(command)
            writer.flush()
            out.write(EOF)
            out.flush()

            var result = readInputStream(inputStream)
            val resultString = String(result)
            var isError = false
            if (resultString.startsWith(ERROR_START)) {
                isError = true
                if (resultString.length > ERROR_START.length) {
                    var resultTemp = ListUtils.getObjectArray(result)
                    resultTemp = resultTemp.subList(ERROR_START.length + 1, resultTemp.size)
                    result = ListUtils.getPrimitiveArray(resultTemp)
                } else {
                    result = ByteArray(0)
                }
            }
            return ShellResult(isError, result)
        } catch (e: IOException) {
            SimpleLog.e(e)
            return ShellResult(true, e.toString().toByteArray())
        } finally {
            socket.safeClose()
            writer.safeClose()
            inputStream.safeClose()
        }
    }

    private fun runCommandInShell(command: String, asRoot: Boolean): ShellResult {
        if (command.isEmpty()) {
            return ShellResult(true, "Command is empty".toByteArray())
        }
        var process: Process? = null
        var inputStream: InputStream? = null
        var errorStream: InputStream? = null
        try {
            process = ProcessBuilder(if (asRoot) "su" else "sh", "-c", command + '\n').start()
            process!!.waitFor()
            inputStream = process.inputStream
            errorStream = process.errorStream

            var isError = true
            var result = readInputStream(errorStream)
            if (result.isEmpty()) {
                isError = false
                result = readInputStream(inputStream)
            }
            return ShellResult(isError, result)
        } catch (e: IOException) {
            SimpleLog.e(e)
            return ShellResult(true, e.toString().toByteArray())
        } catch (e: InterruptedException) {
            SimpleLog.e(e)
            return ShellResult(true, e.toString().toByteArray())
        } finally {
            inputStream.safeClose()
            errorStream.safeClose()
            process.safeDestroy()
        }
    }

    private fun readInputStream(inputStream: InputStream): ByteArray {
        val result = ArrayList<Byte>()
        try {
            val buffer = ByteArray(8096)
            var read: Int
            do {
                read = inputStream.read(buffer)
                for (i in 0 until read) {
                    result.add(buffer[i])
                }
                if (detectAndDeleteEOF(result)) {
                    break
                }
            } while (read >= 0)
            return ListUtils.getPrimitiveArray(result)
        } catch (e: IOException) {
            SimpleLog.e(e)
            return ListUtils.getPrimitiveArray(result)
        }
    }

    private fun detectAndDeleteEOF(data: MutableList<Byte>): Boolean {
        if (data.size < EOF.size) return false
        var startIndex = data.size - EOF.size
        for (aEOF in EOF) {
            if (data[startIndex] != aEOF) {
                return false
            }
            startIndex++
        }
        SimpleLog.d("EOF detected!")
        for (ignored in EOF) {
            data.removeAt(data.size - 1)
        }
        return true
    }


}
