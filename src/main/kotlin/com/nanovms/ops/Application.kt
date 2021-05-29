package com.nanovms.ops

class Application(path: String, proc: OpsProcess) {
    val path = path
    val process = proc

    fun terminate(): Unit {
        process.terminate()
    }
}