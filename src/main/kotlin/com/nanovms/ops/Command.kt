package com.nanovms.ops

import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.File

abstract class Command(val project: Project) {
    private lateinit var monitor: CommandMonitor

    private var _processHandler: OSProcessHandler? = null
    val processHandler: OSProcessHandler?
        get() = _processHandler

    val isAlive: Boolean
        get() = processHandler?.process?.isAlive == true

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

        _processHandler = OSProcessHandler(Runtime.getRuntime().exec(command), command, Charsets.UTF_8)
        _processHandler?.addProcessListener(CommandProcessListener(this))
        _processHandler?.startNotify()
        monitor = CommandMonitor(project, this)
        monitor.start()

        val ops = service<OpsService>()
        ops.holdCommand(this)
    }

    protected abstract fun createArguments(): Collection<String>

    private fun homeDir(): String {
        val userHomeDir = System.getProperty("user.home")
        return userHomeDir + File.separatorChar + ".ops"
    }
}