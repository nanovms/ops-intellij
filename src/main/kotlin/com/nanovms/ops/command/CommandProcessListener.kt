package com.nanovms.ops.command

import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Key
import com.nanovms.ops.Service

class CommandProcessListener(private val command: Command) : ProcessAdapter() {

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}

    override fun startNotified(event: ProcessEvent) {
        val ops = service<Service>()
        ops.println(command.project, "\n\n[exec] ${command.commandLine}")
        command.listener?.started(command)
    }

    override fun processTerminated(event: ProcessEvent) {
        val ops = service<Service>()
        event.text?.let {
            ops.println(command.project, event.text)
        }
        ops.println(command.project, "[terminated] PID #${command.pid}")
        command.listener?.terminated(command)
    }
}