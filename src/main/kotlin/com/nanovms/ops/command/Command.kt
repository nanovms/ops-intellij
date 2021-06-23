package com.nanovms.ops.command

import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.project.Project
import com.nanovms.ops.Ops
import java.io.InputStream

enum class CommandType {
    RunExecutable,
    Other
}

abstract class Command(val project: Project) {
    var name: String = ""
    var type: CommandType = CommandType.Other

    val pid: Long
        get() = _processHandler.process.pid()

    val isAlive: Boolean
        get() = _processHandler.process.isAlive

    val hasError: Boolean
        get() = _processHandler.exitCode != 0

    val commandLine: String
        get() = _processHandler.commandLine

    val inputStream: InputStream
        get() = _processHandler.process.inputStream

    val errorStream: InputStream
        get() = _processHandler.process.errorStream

    private var _listener: CommandListener? = null
    val listener: CommandListener?
        get() = _listener

    fun withListener(listener: CommandListener): Command {
        _listener = listener
        return this
    }

    fun execute() {
        _processHandler = Ops.instance.execute(*createArguments().toTypedArray())
        _processHandler.setShouldDestroyProcessRecursively(true)
        _processHandler?.addProcessListener(CommandProcessListener(this))
        _processHandler?.startNotify()
        monitor = CommandMonitor(project, this)
        monitor.start()

        Ops.instance.holdCommand(this)
    }

    fun stop() {
        _processHandler.destroyProcess()
    }

    protected abstract fun createArguments(): Collection<String>

    private lateinit var monitor: CommandMonitor
    private lateinit var _processHandler: OSProcessHandler
}