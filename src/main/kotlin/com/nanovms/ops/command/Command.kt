package com.nanovms.ops.command

import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nanovms.ops.Service
import java.io.File
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

    private var _listener: CommandListener? = null
    val listener: CommandListener?
        get() = _listener

    fun withListener(listener: CommandListener): Command {
        _listener = listener
        return this
    }

    fun execute() {
        var command = homeDir() + File.separatorChar + "bin" + File.separatorChar + "ops"
        val file = File(command)
        if (!file.exists()) {
            command = "ops"
        }

        val arguments = createArguments()
        if(arguments.isNotEmpty()) {
            for (arg in arguments) {
                command += " $arg"
            }
        }
        command += " --show-debug"

        _processHandler = OSProcessHandler(Runtime.getRuntime().exec(command), command, Charsets.UTF_8)
        _processHandler.setShouldDestroyProcessRecursively(true)
        _processHandler?.addProcessListener(CommandProcessListener(this))
        _processHandler?.startNotify()
        monitor = CommandMonitor(project, this)
        monitor.start()

        val ops = service<Service>()
        ops.holdCommand(this)
    }

    fun stop() {
        _processHandler.destroyProcess()
    }

    protected abstract fun createArguments(): Collection<String>

    private fun homeDir(): String {
        val userHomeDir = System.getProperty("user.home")
        return userHomeDir + File.separatorChar + ".ops"
    }

    private lateinit var monitor: CommandMonitor
    private lateinit var _processHandler: OSProcessHandler
}