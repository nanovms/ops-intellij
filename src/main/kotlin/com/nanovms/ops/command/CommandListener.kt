package com.nanovms.ops.command

import com.intellij.execution.process.ProcessEvent

class CommandEvent(process: ProcessEvent) {
    val process = process

    val hasError: Boolean
        get() = (process.processHandler.exitCode != 0)
}

open class CommandListener {
    open fun started(event: CommandEvent) {}
    open fun terminated(event: CommandEvent) {}
}