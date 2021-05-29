package com.nanovms.ops

import java.io.BufferedReader
import java.io.InputStreamReader

class OpsProcess(proc: Process) {
    private val process = proc

    val pid: Long
        get() = process.pid()

    val terminatedWithError: Boolean
        get() = !process.isAlive && hasError

    val hasError: Boolean
        get() = process.exitValue() != 0

    val output: String
        get() {
            val input = BufferedReader(InputStreamReader(process.inputStream))
            val out = input.readText()
            input.close()
            return out
        }

    val errorOutput: String
        get() {
            val input = BufferedReader(InputStreamReader(process.errorStream))
            val out = input.readText()
            input.close()
            return out
        }

    fun waitFor(): Boolean {
        return process.waitFor() == 0
    }

    fun terminate(): Unit {
        return process.destroy()
    }
}