package com.nanovms.ops.command

import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
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
        _processHandler.addProcessListener(CommandProcessListener(this))
        _processHandler.startNotify()
        _processHandler.addProcessListener(object : ProcessListener {
            override fun startNotified(event: ProcessEvent) {}
            override fun processTerminated(event: ProcessEvent) {}
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                if (!event.text.endsWith('\n')) {
                    // Ops output usually ended with newline,
                    // the one that doesn't usually a progress bar update text.
                    // By skipping this, we can display the progress bar in one line using half of the update,
                    // and the other half are these line without newline.
                    return
                }
                Ops.instance.print(project, event.text)
            }
        })

        Ops.instance.holdCommand(this)
    }

    fun stop() {
        _processHandler.destroyProcess()
    }

    protected abstract fun createArguments(): Collection<String>
    private lateinit var _processHandler: OSProcessHandler
}